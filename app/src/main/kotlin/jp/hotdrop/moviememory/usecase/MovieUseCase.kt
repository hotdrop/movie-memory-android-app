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

    fun prepared(): Completable =
            repository.prepared()
                    .subscribeOn(Schedulers.io())

    fun findNowPlayingMovies(index: Int, offset: Int): Single<List<Movie>> {
        val endAt = LocalDate.now()
        val startAt = endAt.minusMonths(NOW_PLAYING_BETWEEN_MONTH)
        return repository.findNowPlayingMovies(startAt, endAt, index, offset)
                .subscribeOn(Schedulers.io())
    }

    fun movieFlowable(id: Int): Flowable<Movie> =
            repository.movieFlowable(id)
                    .subscribeOn(Schedulers.io())

    fun findMovie(id: Int): Single<Movie> =
            repository.findMovie(id)
                    .subscribeOn(Schedulers.io())

    fun loadRecentMovies(): Completable =
            repository.loadRecentMovies()
                    .subscribeOn(Schedulers.io())

    fun saveLocalEdit(movie: Movie): Completable =
            repository.saveLocalMovieInfo(movie)
                    .subscribeOn(Schedulers.io())

    companion object {
        private const val NOW_PLAYING_BETWEEN_MONTH = 2L
    }
}