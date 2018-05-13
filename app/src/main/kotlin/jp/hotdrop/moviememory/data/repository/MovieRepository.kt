package jp.hotdrop.moviememory.data.repository

import android.support.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Flowable
import jp.hotdrop.moviememory.model.Movie

interface MovieRepository {
    @CheckResult
    fun loadNowPlayingMovies(index: Int = 0, offset: Int = 20): Completable
    // TODO hogeFlagはいずれ消す
    fun nowPlayingMovies(index: Int = 0, offset: Int = 20, hogeFlagTest: Boolean = false): Flowable<List<Movie>>

    fun loadComingSoonMovies(index: Int = 0, offset: Int = 20): Flowable<List<Movie>>
}