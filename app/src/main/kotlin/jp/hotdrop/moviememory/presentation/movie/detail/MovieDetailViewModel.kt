package jp.hotdrop.moviememory.presentation.movie.detail

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
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
    private val mutableMovie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = mutableMovie

    fun loadMovie(id: Int) {
        useCase.movie(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { mutableMovie.postValue(it) },
                        onError = { Timber.e(it) }
                ).addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}