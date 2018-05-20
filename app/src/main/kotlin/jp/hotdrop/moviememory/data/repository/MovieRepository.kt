package jp.hotdrop.moviememory.data.repository

import android.support.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Flowable
import jp.hotdrop.moviememory.model.Movie

interface MovieRepository {

    val nowPlayingMovies: Flowable<List<Movie>>
    @CheckResult
    fun loadNowPlayingMovies(index: Int = 0, offset: Int = 20): Completable
}