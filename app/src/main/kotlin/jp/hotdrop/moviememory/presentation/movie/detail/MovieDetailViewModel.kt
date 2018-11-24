package jp.hotdrop.moviememory.presentation.movie.detail

import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.usecase.MovieUseCase
import jp.hotdrop.moviememory.model.Movie
import timber.log.Timber
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
        private val useCase: MovieUseCase
): ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    var movie: LiveData<Movie>? = null

    fun setUp(id: Int) {
        movie = LiveDataReactiveStreams.fromPublisher(useCase.movie(id))
    }

    fun saveFavorite(count: Int) {
        movie?.value?.let {
            it.favoriteCount = count
            useCase.saveLocalEdit(it)
                    .observeOn(Schedulers.io())
                    .subscribeBy(
                            onComplete = { Timber.d("お気に入りスター数を保存") },
                            onError = { Timber.e(it) }
                    ).addTo(compositeDisposable)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}