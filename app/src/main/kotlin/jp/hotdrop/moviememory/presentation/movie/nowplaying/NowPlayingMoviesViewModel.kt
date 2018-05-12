package jp.hotdrop.moviememory.presentation.movie.nowplaying

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.ViewModel
import jp.hotdrop.moviememory.domain.MoviesUseCase
import jp.hotdrop.moviememory.model.Movie
import javax.inject.Inject

class NowPlayingMoviesViewModel @Inject constructor(
        private val useCase: MoviesUseCase
): ViewModel() {

    val movies: LiveData<List<Movie>> by lazy {
        LiveDataReactiveStreams.fromPublisher(useCase.nowPlayingMovies())
    }
}