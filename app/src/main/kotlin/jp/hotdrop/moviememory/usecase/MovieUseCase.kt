package jp.hotdrop.moviememory.usecase

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

    fun nowPlayingMovies(offset: Int): Flowable<List<Movie>> {
        val endAt = LocalDate.now()
        val startAt = endAt.minusMonths(2L)
        return repository.movies(offset, startAt, endAt)
    }

    fun movie(id: Int): Single<Movie> =
            repository.movie(id)
                    .subscribeOn(Schedulers.io())

    fun loadNowPlayingMovies(index: Int, offset: Int): Completable =
            repository.loadNowPlayingMovies(index, offset)
                    .subscribeOn(Schedulers.io())

    fun refresh(offset: Int): Completable =
            repository.refresh(offset)
                    .subscribeOn(Schedulers.io())
}