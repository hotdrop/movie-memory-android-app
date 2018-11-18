package jp.hotdrop.moviememory.data.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.MovieDatabase
import jp.hotdrop.moviememory.data.local.entity.LocalMovieInfoEntity
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

    override fun findMovies(startIndex: Int, offset: Int, startAt: LocalDate, endAt: LocalDate): Single<List<Movie>> =
            movieDatabase.findMovies(startAt, endAt)
                    .map {
                        val endIndex = if (startIndex + offset < it.size) {
                            startIndex + offset - 1
                        } else {
                            it.size - 1
                        }
                        println("  $startIndex から $endIndex の映画情報を取得")
                        it.subList(startIndex, endIndex)
                        // TODO startIndexが保持量以上なら何もしない
                    }.map { movieEntities ->
                        movieEntities.map { entity ->
                            val localMovieInfo = movieDatabase.getLocalMovieInfo(entity.id)
                            entity.toMovie(localMovieInfo)
                        }
                    }

    override fun movie(id: Int): Single<Movie> =
        movieDatabase.getMovie(id)
                .map { movieEntity ->
                    Timber.d("映画情報詳細を取得。id=$id")
                    val localMovieInfo = movieDatabase.getLocalMovieInfo(movieEntity.id)
                    movieEntity.toMovie(localMovieInfo)
                }

    /**
     * 最新の映画情報を取得する
     */
    override fun loadRecentMovies(): Completable =
        movieDatabase.getRecentMovieId()
                .flatMapCompletable {
                    refresh(it)
                }

    /**
     * ネットワークから最新データを取得
     */
    private fun refresh(fromMovieId: Int? = null): Completable =
    // TODO 開発中、APIがまともに動かないのでダミーAPI（ローカルでデータを生成する）を使う。この状態だとUnitTest通らないので注意
//            api.getNowPlaying(fromMovieId)
            dummyApi.getMovies(fromMovieId)
                    .doOnSuccess { movieResults ->
                        Timber.d("  取得した件数=${movieResults.size}")
                        val movieEntities = movieResults.map { it.toMovieEntity() }
                        movieDatabase.save(movieEntities)
                    }.doOnError {
                        Timber.e(it, "映画情報の読み込みに失敗")
                    }.toCompletable()

    private fun Movie.toLocal(): LocalMovieInfoEntity =
            LocalMovieInfoEntity(
                    this.id,
                    this.isSaw,
                    this.sawDate?.toEpochDay(),
                    this.sawPlace,
                    this.memo
            )

    override fun saveLocalMovieInfo(movie: Movie): Completable =
        Completable.create {
            Timber.d("編集した値を保存します。isSaw = ${movie.isSaw}, sawDate=${movie.sawDate}, sawPlace=${movie.sawPlace}")
            movieDatabase.saveLocalInfo(movie.toLocal())
            it.onComplete()
        }
}