package jp.hotdrop.moviememory.data.repository

import android.support.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.model.Movie

interface MovieRepository {

    @CheckResult
    fun moviesByPlayingDate(offset: Int): Flowable<List<Movie>>
    @CheckResult
    fun movie(id: Int): Single<Movie>

    @CheckResult
    fun loadNowPlayingMovies(index: Int, offset: Int): Completable
    @CheckResult
    fun refreshNowPlayingMovies(offset: Int): Completable
}