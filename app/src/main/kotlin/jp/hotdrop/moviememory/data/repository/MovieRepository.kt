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
    fun findMovies(startIndex: Int, offset: Int, startAt: LocalDate, endAt: LocalDate): Single<List<Movie>>

    @CheckResult
    fun movie(id: Int): Single<Movie>

    @CheckResult
    fun loadRecentMovies(): Completable

    @CheckResult
    fun saveLocalMovieInfo(movie: Movie): Completable
}