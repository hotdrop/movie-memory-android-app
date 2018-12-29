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
    fun loadRecentMovies(): Completable

    @CheckResult
    fun findNowPlayingMovies(startAt: LocalDate, endAt: LocalDate, startIndex: Int, offset: Int): Single<List<Movie>>

    @CheckResult
    fun findComingSoonMovies(startAt: LocalDate, startIndex: Int, offset: Int): Single<List<Movie>>

    @CheckResult
    fun findPastMovies(startAt: LocalDate, startIndex: Int, offset: Int): Single<List<Movie>>

    @CheckResult
    fun clearMovies(): Completable

    @CheckResult
    fun movieFlowable(id: Long): Flowable<Movie>

    @CheckResult
    fun findMovie(id: Long): Single<Movie>

    @CheckResult
    fun saveLocalMovieInfo(movie: Movie): Completable
}