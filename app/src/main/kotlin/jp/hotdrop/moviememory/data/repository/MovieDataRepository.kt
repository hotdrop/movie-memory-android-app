package jp.hotdrop.moviememory.data.repository

import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.data.remote.MovieApi
import jp.hotdrop.moviememory.data.remote.mapper.toMovies
import javax.inject.Inject

class MovieDataRepository @Inject constructor(
        private val api: MovieApi
): MovieRepository {
    //override val nowPlayingMovies: Flowable<List<Movie>> =

    override fun loadNowPlayingMovies(index: Int, offset: Int): Completable {
        return api.getNowPraying(index, offset)
                .map { it.toMovies() }
                .doOnSuccess {
                    // TODO DEBUGならTimberに結果出力
                    // TODO DBに保持する
                }
                .subscribeOn(Schedulers.io())
                .toCompletable()
    }
}