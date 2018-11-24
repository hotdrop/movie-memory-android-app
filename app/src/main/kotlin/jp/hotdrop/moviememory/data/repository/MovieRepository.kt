package jp.hotdrop.moviememory.data.repository

import androidx.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.model.Movie

interface MovieRepository {

    @CheckResult
    fun prepared(): Completable

    @CheckResult
    fun findNowPlayingMovies(startIndex: Int, offset: Int): Single<List<Movie>>

    @CheckResult
    fun loadRecentMovies(): Completable

    @CheckResult
    fun movieFlowable(id: Int): Flowable<Movie>

    @CheckResult
    fun findMovie(id: Int): Single<Movie>

    @CheckResult
    fun saveLocalMovieInfo(movie: Movie): Completable
}