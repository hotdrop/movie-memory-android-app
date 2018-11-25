package jp.hotdrop.moviememory.data.repository

import androidx.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.model.Movie
import org.threeten.bp.LocalDate

interface MovieRepository {

    @CheckResult
    fun prepared(): Completable

    @CheckResult
    fun findNowPlayingMovies(startAt: LocalDate, endAt: LocalDate, startIndex: Int, offset: Int): Single<List<Movie>>

    @CheckResult
    fun findUnReleasePlayingMovies(startAt: LocalDate, startIndex: Int, offset: Int): Single<List<Movie>>

    @CheckResult
    fun findPastMovies(startAt: LocalDate, startIndex: Int, offset: Int): Single<List<Movie>>

    @CheckResult
    fun loadRecentMovies(): Completable

    @CheckResult
    fun movieFlowable(id: Int): Flowable<Movie>

    @CheckResult
    fun findMovie(id: Int): Single<Movie>

    @CheckResult
    fun saveLocalMovieInfo(movie: Movie): Completable
}