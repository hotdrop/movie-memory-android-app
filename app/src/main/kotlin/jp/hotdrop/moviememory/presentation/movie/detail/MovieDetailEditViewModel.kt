package jp.hotdrop.moviememory.presentation.movie.detail

import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.usecase.MovieUseCase
import timber.log.Timber
import javax.inject.Inject

class MovieDetailEditViewModel @Inject constructor(
        private val useCase: MovieUseCase
): ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    var movie: LiveData<Movie>? = null

    private val mutableSaveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> = mutableSaveSuccess

    fun setUp(id: Int) {
        movie = LiveDataReactiveStreams.fromPublisher(useCase.movieFlowable(id))
    }

    fun save(sawDateStr: String) {
        val movie = movie?.value ?: return
        if (sawDateStr.isNotEmpty()) {
            movie.setWatchDateFromText(sawDateStr)
        }
        useCase.saveLocalEdit(movie)
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = { mutableSaveSuccess.postValue(true) },
                        onError = { Timber.e(it) }
                ).addTo(compositeDisposable)
    }

    fun clear() {
        mutableSaveSuccess.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}