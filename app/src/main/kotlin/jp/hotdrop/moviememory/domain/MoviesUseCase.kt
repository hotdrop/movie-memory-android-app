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
     * TODO ProcessorにしないとUseCaseの意味ない気がするのでそうする。ただ今の所やることないのでデータをもらって流すだけ・・
     */
    fun nowPlayingMovies(index: Int, offset: Int): Flowable<List<Movie>> =
            repository.nowPlayingMovies(index, offset)

    fun refreshNowPlayingMovies(index: Int, offset: Int): Completable =
        repository.loadNowPlayingMovies(index, offset)
                .subscribeOn(Schedulers.io())
}