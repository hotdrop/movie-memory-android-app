package jp.hotdrop.moviememory.data.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.MovieDatabase
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import jp.hotdrop.moviememory.data.local.entity.toLocal
import jp.hotdrop.moviememory.data.local.entity.toMovie
import jp.hotdrop.moviememory.data.remote.DummyApi
import jp.hotdrop.moviememory.data.remote.MovieApi
import jp.hotdrop.moviememory.data.remote.response.toMovieEntity
import jp.hotdrop.moviememory.model.Movie
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

class MovieDataRepository @Inject constructor(
        private val api: MovieApi,
        private val movieDatabase: MovieDatabase
): MovieRepository {

    // TODO ダミー！
    private val dummyApi = DummyApi()

    /**
     * ローカルに1件もデータがない場合に限り、リモートから全データを取得してDBに保持する
     */
    override fun prepared(): Completable =
            movieDatabase.isExist()
                    .flatMapCompletable {
                        if (it) {
                            Completable.complete()
                        } else {
                            refresh()
                        }
                    }

    override fun findNowPlayingMovies(startAt: LocalDate, endAt: LocalDate, startIndex: Int, offset: Int): Single<List<Movie>> =
            movieDatabase.findMoviesByBetween(startAt, endAt)
                    .map { takeTheRange(startIndex, offset, it) }
                    .map {
                        it.map { entity -> entityToMovieWithLocalInfo(entity) }
                    }

    override fun findComingSoonMovies(startAt: LocalDate, startIndex: Int, offset: Int): Single<List<Movie>> =
            movieDatabase.findMoviesByAfter(startAt)
                    .map { takeTheRange(startIndex, offset, it) }
                    .map {
                        it.map { entity -> entityToMovieWithLocalInfo(entity) }
                    }

    override fun findPastMovies(startAt: LocalDate, startIndex: Int, offset: Int): Single<List<Movie>> =
            movieDatabase.findMoviesByBefore(startAt)
                    .map { takeTheRange(startIndex, offset, it) }
                    .map {
                        it.map { entity -> entityToMovieWithLocalInfo(entity) }
                    }

    /**
     * 保持している映画情報の最新IDから、それ以降に登録された映画情報がないかリモートに問い合わせて取得する
     */
    override fun loadRecentMovies(): Completable =
            movieDatabase.findRecentMovieId()
                    .flatMapCompletable {
                        refresh(it)
                    }

    /**
     * 1つの映画情報を取得する。（Flowable）
     */
    override fun movieFlowable(id: Int): Flowable<Movie> =
            movieDatabase.movieFlowable(id)
                    .map {
                        entityToMovieWithLocalInfo(it)
                    }

    /**
     * 1つの映画情報を取得する。（Single）
     */
    override fun findMovie(id: Int): Single<Movie> =
            movieDatabase.findMovie(id)
                    .map {
                        entityToMovieWithLocalInfo(it)
                    }

    override fun saveLocalMovieInfo(movie: Movie): Completable =
            Completable.create {
                Timber.d("編集した値を保存します。watchDate=${movie.watchDate}, watchPlace=${movie.watchPlace}")
                movieDatabase.saveLocalInfo(movie.toLocal())
                it.onComplete()
            }

    /**
     * ネットワークから最新データを取得
     */
    private fun refresh(fromMovieId: Int? = null): Completable =
    // TODO 開発中、APIがまともに動かないのでダミーAPI（ローカルでデータを生成する）を使う。この状態だとUnitTest通らないので注意
//            api.getNowPlaying(fromMovieId)
            dummyApi.nowPlaying(fromMovieId)
                    .doOnSuccess {
                        Timber.d("  取得した件数=${it.size}")
                        val movieEntities = it.map { movieResult ->
                            movieResult.toMovieEntity()
                        }
                        movieDatabase.save(movieEntities)
                    }.doOnError {
                        Timber.e(it, "映画情報の読み込みに失敗")
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

    private fun entityToMovieWithLocalInfo(entity: MovieEntity): Movie {
        val localMovieInfo = movieDatabase.findLocalMovieInfo(entity.id)
        return entity.toMovie(localMovieInfo)
    }
}