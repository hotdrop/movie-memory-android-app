package jp.hotdrop.moviememory.data.repository

import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.CategoryDatabase
import jp.hotdrop.moviememory.data.local.MovieDatabase
import jp.hotdrop.moviememory.data.local.MovieNoteDatabase
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import jp.hotdrop.moviememory.data.local.entity.toEntity
import jp.hotdrop.moviememory.data.local.entity.toLocal
import jp.hotdrop.moviememory.data.local.entity.toMovie
import jp.hotdrop.moviememory.data.remote.MovieApi
import jp.hotdrop.moviememory.data.remote.response.toEntity
import jp.hotdrop.moviememory.model.Movie
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

@Reusable
class MovieRepository @Inject constructor(
        private val movieApi: MovieApi,
        private val movieDatabase: MovieDatabase,
        private val movieNoteDatabase: MovieNoteDatabase,
        private val categoryDatabase: CategoryDatabase
) {

    /**
     * ローカルに1件もデータがない場合に限り、リモートから全データを取得してDBに保持する
     */
    fun prepared(): Completable =
            movieDatabase.isExist()
                    .flatMapCompletable {
                        if (it) {
                            Completable.complete()
                        } else {
                            refresh()
                        }
                    }

    /**
     * TODO 最新データを取得する。最新をどうするか考える必要が出たのでここ保留
     */
    fun loadRecentMovies(): Completable {
        return Completable.complete()
    }

    fun findNowPlayingMovies(startAt: LocalDate, endAt: LocalDate, startIndex: Int, offset: Int): Single<List<Movie>> =
            movieDatabase.findMoviesByBetween(startAt, endAt)
                    .map { takeTheRange(startIndex, offset, it) }
                    .map {
                        it.map { entity -> entityToMovieWithLocalInfo(entity) }
                    }

    fun findComingSoonMovies(startAt: LocalDate, startIndex: Int, offset: Int): Single<List<Movie>> =
            movieDatabase.findMoviesByAfter(startAt)
                    .map { takeTheRange(startIndex, offset, it) }
                    .map {
                        it.map { entity -> entityToMovieWithLocalInfo(entity) }
                    }

    fun findPastMovies(startAt: LocalDate, startIndex: Int, offset: Int): Single<List<Movie>> =
            movieDatabase.findMoviesByBefore(startAt)
                    .map { takeTheRange(startIndex, offset, it) }
                    .map {
                        it.map { entity -> entityToMovieWithLocalInfo(entity) }
                    }

    fun clearMovies(): Completable =
            Completable.create {
                Timber.d("映画情報をクリアします。")
                movieDatabase.deleteAll()
                it.onComplete()
            }

    /**
     * 1つの映画情報を取得する。（Flowable）
     */
    fun movieFlowable(id: Long): Flowable<Movie> =
            movieDatabase.movieWithFlowable(id)
                    .map {
                        entityToMovieWithLocalInfo(it)
                    }

    /**
     * 1つの映画情報を取得する。（Single）
     */
    fun findMovie(id: Long): Single<Movie> =
            movieDatabase.find(id)
                    .map {
                        entityToMovieWithLocalInfo(it)
                    }

    fun save(movie: Movie): Completable =
            Completable.create {
                movieDatabase.saveMovie(movie.toEntity())
                it.onComplete()
            }

    private fun entityToMovieWithLocalInfo(entity: MovieEntity): Movie {
        val localMovieInfo = movieNoteDatabase.find(entity.id)
        return entity.toMovie(localMovieInfo, categoryDatabase)
    }

    fun saveLocalMovieInfo(movie: Movie): Completable =
            Completable.create {
                Timber.d("編集した値を保存します。watchDate=${movie.watchDate}, watchPlace=${movie.watchPlace}")
                movieNoteDatabase.save(movie.toLocal())
                it.onComplete()
            }

    /**
     * ネットワークから最新データを取得
     */
    private fun refresh(): Completable =
            movieApi.findAll()
                    .map { responses ->
                        Timber.d("  取得した件数=${responses.size}")

                        val categoryMap = mutableMapOf<String, Long>()
                        responses.map { it.categoryName }
                                .forEach { categoryName ->
                                    categoryMap[categoryName] = categoryDatabase.register(categoryName)
                                }

                        val movieEntities = responses.map { movieResponse ->
                            movieResponse.toEntity(categoryMap)
                        }
                        movieDatabase.save(movieEntities)

                    }.toCompletable()

    private fun takeTheRange(fromIndex: Int, offset: Int, movieEntities: List<MovieEntity>): List<MovieEntity> {
        val listSize = movieEntities.size
        return when {
            fromIndex >= listSize -> {
                Timber.d("  最後まで取得済みのため何もしない")
                listOf()
            }
            else -> {
                val toIndex = if (fromIndex + offset < listSize) {
                    fromIndex + offset - 1
                } else {
                    listSize - 1
                }
                Timber.d("  $fromIndex から $toIndex の映画情報を取得")
                movieEntities.subList(fromIndex, toIndex)
            }
        }
    }
}