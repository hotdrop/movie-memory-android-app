package jp.hotdrop.moviememory.data.repository

import android.support.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.model.Movie
import org.threeten.bp.LocalDate

interface MovieRepository {

    @CheckResult
    fun movies(offset: Int, startAt: LocalDate, endAt: LocalDate): Flowable<List<Movie>>

    @CheckResult
    fun movie(id: Int): Single<Movie>

    @CheckResult
    fun loadNowPlayingMovies(index: Int, offset: Int): Completable

    // TODO 無条件で全リフレッシュが本当にいいか考える
    @CheckResult
    fun refresh(offset: Int): Completable

    @CheckResult
    fun saveLocalMovieInfo(movie: Movie): Completable
}