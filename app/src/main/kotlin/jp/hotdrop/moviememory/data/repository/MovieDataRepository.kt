package jp.hotdrop.moviememory.data.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.MovieDatabase
import jp.hotdrop.moviememory.data.local.entity.toMovie
import jp.hotdrop.moviememory.data.remote.MovieApi
import jp.hotdrop.moviememory.data.remote.response.MovieResult
import jp.hotdrop.moviememory.data.remote.response.toMovieEntity
import jp.hotdrop.moviememory.model.Movie
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

class MovieDataRepository @Inject constructor(
        private val api: MovieApi,
        private val movieDatabase: MovieDatabase
): MovieRepository {

    /**
     * DBからデータ取得する
     * 全データ持ってきてFlatMapで絞っているので一時的にメモリ上に置いているの、もっといいやり方はないものか
     */
    override fun movies(offset: Int, startAt: LocalDate, endAt: LocalDate): Flowable<List<Movie>> =
        movieDatabase.getMovies(startAt, endAt)
                .filter { it.isNotEmpty() }
                .flatMap {
                    val startIdx = it.size - offset
                    Flowable.fromArray(it.subList(startIdx, it.size))
                }
                .map { movieEntities ->
                    Timber.d("DBから映画情報を取得。件数=${movieEntities.size}")
                    movieEntities.forEach {
                        Timber.d("  取得した映画情報のタイトル: ${it.title}")
                    }
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
     * ネットワークから最新データを取得してDBをリフレッシュする。
     * startAtとendAtがnullなら全データをリフレッシュする
     */
    override fun refresh(offset: Int, startAt: LocalDate?, endAt: LocalDate?): Completable =
            // 開発中、API通信なしでデータを取得したい場合にこっち使う。
            dummyGetNowPlaying(0, offset)
            //api.getNowPlaying(0, offset)
                    .doOnSuccess { movieResults ->
                        Timber.d("公開中の映画情報を取得。件数=${movieResults.size}")
                        movieResults.forEach {
                            Timber.d("  取得した映画情報のタイトル: ${it.title}")
                        }
                        val movieEntities = movieResults.map { it.toMovieEntity() }
                        movieDatabase.refresh(movieEntities, startAt, endAt)
                    }.doOnError {
                        Timber.e(it, "公開中の映画情報の読み込みに失敗")
                    }.toCompletable()

    /**
     * 公開中の映画情報を取得する
     */
    override fun loadNowPlayingMovies(index: Int, offset: Int): Completable =
            // 開発中、API通信なしでデータを取得したい場合にこっち使う。
            dummyGetNowPlaying(index, offset)
            //api.getNowPlaying(index, offset)
                    .doOnSuccess { movieResults ->
                        Timber.d("API経由で公開中の映画情報を取得。件数=${movieResults.size}")
                        movieResults.forEach {
                            Timber.d("  取得した映画情報のタイトル: ${it.title}")
                        }
                        val movieEntities = movieResults.map { it.toMovieEntity() }
                        movieDatabase.save(movieEntities)
                    }.doOnError {
                        Timber.e(it, "公開中の映画情報の読み込みに失敗")
                    }.toCompletable()

    private fun dummyGetNowPlaying(index: Int, offset: Int): Single<List<MovieResult>> =
        Single.just(
                (index..(index + offset - 1)).map { createDummyResponse(it) }
        )

    private fun createDummyResponse(id: Int): MovieResult =
            MovieResult(
                    id,
                    "テスト$id",
                "ここは概要が入ります。映画のあらすじなどで本当は編集可能にしたい。$id",
                    "",
                    "2018-05-10",
                    "Directorです",
                    "https://urltest.test",
                    "https://movieurl.test",
                    "2018-05-20T12:34:56"
            )
}