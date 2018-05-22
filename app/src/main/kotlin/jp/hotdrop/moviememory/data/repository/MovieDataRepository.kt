package jp.hotdrop.moviememory.data.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.MovieDatabase
import jp.hotdrop.moviememory.data.local.entity.toNowPlayingMovie
import jp.hotdrop.moviememory.data.remote.MovieApi
import jp.hotdrop.moviememory.data.remote.response.MovieResult
import jp.hotdrop.moviememory.data.remote.response.toMovieEntity
import jp.hotdrop.moviememory.model.Movie
import timber.log.Timber
import javax.inject.Inject

class MovieDataRepository @Inject constructor(
        private val api: MovieApi,
        private val movieDatabase: MovieDatabase
): MovieRepository {

    override fun nowPlayingMovies(index: Int, offset: Int): Flowable<List<Movie>> =
        movieDatabase.getNowPlayingMovies(index, offset)
                .map { movieEntities ->
                    Timber.d("DBから映画情報を取得。件数=${movieEntities.size}")
                    movieEntities.forEach {
                        Timber.d("  取得した映画情報のタイトル: ${it.title}")
                    }
                    movieEntities.map { entity ->
                        val localMovieInfo = movieDatabase.getLocalMovieInfo(entity.id)
                        entity.toNowPlayingMovie(localMovieInfo)
                    }
                }
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
                (1..4).map { createDummyResponse(it) }
        )

    private fun createDummyResponse(id: Int): MovieResult =
            MovieResult(
                    id,
                    "テスト$id",
                "概要$id",
                    "https://imageUrl.test",
                    "2018-05-10",
                    "Directorです",
                    "https://urltest.test",
                    "https://movieurl.test",
                    "2018-05-20T12:34:56"
            )
}