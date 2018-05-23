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

    fun nowPlayingMovies(offset: Int): Flowable<List<Movie>> =
            repository.nowPlayingMovies(offset)

    fun refreshNowPlayingMovies(offset: Int): Completable =
        repository.refreshNowPlayingMovies(offset)
                .subscribeOn(Schedulers.io())

    fun loadNowPlayingMovies(index: Int, offset: Int): Completable =
            repository.loadNowPlayingMovies(index, offset)
                    .subscribeOn(Schedulers.io())
}