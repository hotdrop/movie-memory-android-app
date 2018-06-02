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

    // refreshを呼ぶ際は必ずstartAtとendAtを意識する必要があるのでデフォルト値は指定しない。
    @CheckResult
    fun refresh(offset: Int, startAt: LocalDate?, endAt: LocalDate?): Completable

    @CheckResult
    fun loadNowPlayingMovies(index: Int, offset: Int): Completable

}