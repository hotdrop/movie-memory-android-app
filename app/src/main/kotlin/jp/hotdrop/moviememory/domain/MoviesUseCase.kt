package jp.hotdrop.moviememory.domain

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.data.repository.MovieRepository
import jp.hotdrop.moviememory.model.Movie
import javax.inject.Inject

class MoviesUseCase @Inject constructor(
        private val repository: MovieRepository
) {

    /**
     * 今の所こいつがある意味はない
     */
    fun nowPlayingMovies(): Flowable<List<Movie>> =
            repository.nowPlayingMovies

    fun loadNowPlayingMovies(): Completable =
        repository.loadNowPlayingMovies()
                .subscribeOn(Schedulers.io())
}