package jp.hotdrop.moviememory.presentation.movie.nowplaying

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.ViewModel
import jp.hotdrop.moviememory.data.repository.MovieRepository
import jp.hotdrop.moviememory.model.Movie
import javax.inject.Inject

class NowPlayingMoviesViewModel @Inject constructor(
        private val repository: MovieRepository
): ViewModel() {

    val movies: LiveData<List<Movie>> by lazy {
        LiveDataReactiveStreams.fromPublisher(repository.loadNowPlayingMovies(hogeFlagTest = true))
    }
}