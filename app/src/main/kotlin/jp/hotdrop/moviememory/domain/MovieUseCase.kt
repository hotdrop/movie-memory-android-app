package jp.hotdrop.moviememory.domain

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.data.repository.MovieRepository
import jp.hotdrop.moviememory.model.Movie
import org.threeten.bp.LocalDate
import javax.inject.Inject

class MovieUseCase @Inject constructor(
        private val repository: MovieRepository
) {

    // 公開日から2ヶ月以内の映画を公開中とする。refreshがあるのでメンバでlazy定義する
    private val nowPlayingStartAt by lazy { nowPlayingEndAt.minusMonths(2L) }
    private val nowPlayingEndAt by lazy { LocalDate.now() }

    fun nowPlayingMovies(offset: Int): Flowable<List<Movie>> =
        repository.movies(offset, nowPlayingStartAt, nowPlayingEndAt)

    fun movie(id: Int): Single<Movie> =
            repository.movie(id)
                    .subscribeOn(Schedulers.io())

    fun refreshNowPlayingMovies(offset: Int): Completable =
        repository.refresh(offset, nowPlayingStartAt, nowPlayingEndAt)
                .subscribeOn(Schedulers.io())

    fun loadNowPlayingMovies(index: Int, offset: Int): Completable =
            repository.loadNowPlayingMovies(index, offset)
                    .subscribeOn(Schedulers.io())
}