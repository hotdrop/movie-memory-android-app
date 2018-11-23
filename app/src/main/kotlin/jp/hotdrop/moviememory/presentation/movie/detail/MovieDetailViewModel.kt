package jp.hotdrop.moviememory.presentation.movie.detail

import androidx.lifecycle.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import jp.hotdrop.moviememory.usecase.MovieUseCase
import jp.hotdrop.moviememory.model.Movie
import timber.log.Timber
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
        private val useCase: MovieUseCase
): ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    var movie: LiveData<Movie>? = null

    private val mutableSaveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> = mutableSaveSuccess

    fun setUp(id: Int) {
        movie = LiveDataReactiveStreams.fromPublisher(useCase.movie(id))
    }

    fun save(sawDateStr: String) {
        val movie = movie?.value ?: return
        if (sawDateStr.isNotEmpty()) {
            movie.setWatchDateFromText(sawDateStr)
        }
        useCase.saveLocalEdit(movie)
                .observeOn(AndroidSchedulers.mainThread())
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