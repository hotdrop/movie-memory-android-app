package jp.hotdrop.moviememory.domain

import io.reactivex.Flowable
import jp.hotdrop.moviememory.data.repository.MovieRepository
import jp.hotdrop.moviememory.model.Movie
import javax.inject.Inject

class MoviesUseCase @Inject constructor(
        private val repository: MovieRepository
) {

    /**
     * 今の所こいつがある意味はない
     */
    fun runNowPlayingMovies(): Flowable<List<Movie>> {
        return repository.nowPlayingMovies
    }
}