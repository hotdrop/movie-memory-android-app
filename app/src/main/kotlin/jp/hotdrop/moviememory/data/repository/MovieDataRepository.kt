package jp.hotdrop.moviememory.data.repository

import io.reactivex.Single
import jp.hotdrop.moviememory.data.remote.MovieApi
import jp.hotdrop.moviememory.data.remote.mapper.toMovies
import jp.hotdrop.moviememory.model.Movie
import javax.inject.Inject

class MovieDataRepository @Inject constructor(
        private val api: MovieApi
): MovieRepository {
    //override val nowPlayingMovies: Flowable<List<Movie>> =

    // DBに一度保存するパターン。これはあとで
    /*
    override fun loadNowPlayingMovies(index: Int, offset: Int): Completable {
        return api.getNowPraying(index, offset)
                .map { it.toMovies() }
                .doOnSuccess {
                    // TODO DEBUGならTimberに結果出力
                    // TODO DBに保持する
                }
                .subscribeOn(Schedulers.io())
                .toCompletable()
    }*/

    /**
     * TODO MovieResponse直下のあたいは今完全に無視
     */
    override fun loadNowPlayingMovies(index: Int, offset: Int): Single<List<Movie>> {
        return api.getNowPraying(index, offset)
                .map { it.toMovies() }
    }
}