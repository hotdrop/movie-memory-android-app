package jp.hotdrop.moviememory.domain

import io.reactivex.Flowable
import jp.hotdrop.moviememory.data.repository.MovieRepository
import jp.hotdrop.moviememory.model.Movie
import javax.inject.Inject

class MoviesUseCase @Inject constructor(
        private val repository: MovieRepository
) {

    fun nowPlayingMovies(): Flowable<List<Movie>> {
        return repository.nowPlayingMovies(0, 20, true)
    }
}