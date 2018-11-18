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
        // これ絶対Repositoryに持っていくべき・・
        val endAt = LocalDate.now()
        val startAt = endAt.minusMonths(2L)
        return repository.findMovies(index, offset, startAt, endAt)
                .subscribeOn(Schedulers.io())
    }

    fun movie(id: Int): Single<Movie> =
            repository.movie(id)
                    .subscribeOn(Schedulers.io())

    fun loadRecentMovies(): Completable =
            repository.loadRecentMovies()
                    .subscribeOn(Schedulers.io())

    fun saveLocalEdit(movie: Movie): Completable =
            repository.saveLocalMovieInfo(movie)
                    .subscribeOn(Schedulers.io())
}