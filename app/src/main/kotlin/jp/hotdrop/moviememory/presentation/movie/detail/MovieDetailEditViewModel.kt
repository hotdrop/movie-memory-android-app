package jp.hotdrop.moviememory.presentation.movie.detail

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.usecase.MovieUseCase
import timber.log.Timber
import javax.inject.Inject

class MovieDetailEditViewModel @Inject constructor(
        private val useCase: MovieUseCase
): ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    private val mutableMovie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = mutableMovie

    private val mutableSaveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> = mutableSaveSuccess

    fun loadMovie(id: Int) {
        useCase.movie(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { mutableMovie.postValue(it) },
                        onError = { Timber.e(it) }
                ).addTo(compositeDisposable)
    }

    fun saveMovie(sawDateStr: String) {
        val movie = movie.value ?: return
        if (sawDateStr.isNotEmpty()) {
            movie.setSawDateFromText(sawDateStr)
        }
        useCase.saveLocalEdit(movie)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = { mutableSaveSuccess.postValue(true) },
                        onError = { Timber.e(it) }
                ).addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}