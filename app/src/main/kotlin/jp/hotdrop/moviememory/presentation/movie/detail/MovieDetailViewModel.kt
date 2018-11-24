package jp.hotdrop.moviememory.presentation.movie.detail

import androidx.lifecycle.*
import jp.hotdrop.moviememory.usecase.MovieUseCase
import jp.hotdrop.moviememory.model.Movie
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
        private val useCase: MovieUseCase
): ViewModel(), LifecycleObserver {

    var movie: LiveData<Movie>? = null

    fun setUp(id: Int) {
        movie = LiveDataReactiveStreams.fromPublisher(useCase.movie(id))
    }
}