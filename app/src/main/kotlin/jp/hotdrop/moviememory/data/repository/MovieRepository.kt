package jp.hotdrop.moviememory.data.repository

import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.SharedPreferences
import jp.hotdrop.moviememory.data.local.database.CategoryDatabase
import jp.hotdrop.moviememory.data.local.database.MovieDatabase
import jp.hotdrop.moviememory.data.local.database.MovieNoteDatabase
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import jp.hotdrop.moviememory.data.local.entity.toEntity
import jp.hotdrop.moviememory.data.local.entity.toLocal
import jp.hotdrop.moviememory.data.local.entity.toMovie
import jp.hotdrop.moviememory.data.remote.api.MovieApi
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
        private val categoryDatabase: CategoryDatabase,
        private val sharedPreferences: SharedPreferences
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
     * 最新データを取得
     * サーバー側の映画情報は、取得した日時のcreateAtが保存される。
     * そのcreateAtはアプリ側でそのまま保持しているので、前回サーバーから取得した映画情報の中で最新の
     * createAtを条件に、これより新しいものをくれ、と投げればまだもらってない最新データだけ取得できる。
     */
    fun loadRecentMovies(): Completable {
        return movieDatabase.findMaxCreatedAt()
                .flatMapCompletable {
                    Timber.d("最新データを取得します。createAt=$it")
                    refresh(it)
                }
    }

    fun findNowPlayingMovies(startAt: LocalDate, endAt: LocalDate, startIndex: Int, offset: Int): Single<List<Movie>> =
            movieDatabase.findMoviesByBetween(startAt, endAt)
                    .map { findTheRange(startIndex, offset, it) }
                    .map { movieEntities ->
                        movieEntities.map { entityToMovieWithLocalInfo(it) }
                    }

    fun findComingSoonMovies(startAt: LocalDate, startIndex: Int, offset: Int): Single<List<Movie>> =
            movieDatabase.findMoviesByAfter(startAt)
                    .map { findTheRange(startIndex, offset, it) }
                    .map { movieEntities ->
                        movieEntities.map { entityToMovieWithLocalInfo(it) }
                    }

    fun findPastMovies(startAt: LocalDate, startIndex: Int, offset: Int): Single<List<Movie>> =
            movieDatabase.findMoviesByBefore(startAt)
                    .map { findTheRange(startIndex, offset, it) }
                    .map { movieEntities ->
                        movieEntities.map { entityToMovieWithLocalInfo(it) }
                    }

    fun count(): Single<Long> = movieDatabase.count()

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
                        Timber.d("映画情報取得 id=$id")
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

    fun findDateOfGetMovieFromRemote(): Single<Long> =
            Single.just(sharedPreferences.dateOfLastGetMovieFromRemote)

    /**
     * ネットワークから最新データを取得
     */
    private fun refresh(fromCreatedAt: Long = 0L): Completable {

        val singleMovies = if (fromCreatedAt == 0L) {
            movieApi.findAll()
        } else {
            movieApi.find(fromCreatedAt)
        }

        return singleMovies.map { responses ->
            Timber.d("  取得した件数=${responses.size}")

            val categoryMap = mutableMapOf<String, Long>()
            responses.map { it.categoryName }
                    .forEach { categoryName ->
                        categoryMap[categoryName] = categoryDatabase.registerForChecking(categoryName)
                    }
            val movieEntities = responses.map { movieResponse ->
                movieResponse.toEntity(categoryMap)
            }
            movieDatabase.save(movieEntities)
            sharedPreferences.dateOfLastGetMovieFromRemote = LocalDate.now().toEpochDay()
        }.ignoreElement()
    }

    private fun findTheRange(fromIndex: Int, offset: Int, movieEntities: List<MovieEntity>): List<MovieEntity> {
        val listSize = movieEntities.size
        Timber.d("全取得サイズ = $listSize")
        return when {
            fromIndex >= listSize -> {
                Timber.d("  最後まで取得済みのため何もしない")
                listOf()
            }
            else -> {
                val toIndex = if (fromIndex + offset < listSize) {
                    fromIndex + offset
                } else {
                    listSize
                }
                Timber.d("  $fromIndex から $toIndex の映画情報を取得")
                movieEntities.subList(fromIndex, toIndex)
            }
        }
    }
}