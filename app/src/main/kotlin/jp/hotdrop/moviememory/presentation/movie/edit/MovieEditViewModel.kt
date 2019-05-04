package jp.hotdrop.moviememory.presentation.movie.edit

import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.model.AppError
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.usecase.MovieUseCase
import timber.log.Timber
import javax.inject.Inject

class MovieEditViewModel @Inject constructor(
        private val useCase: MovieUseCase
): ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    private val mutableMovie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = mutableMovie

    private val mutableSaveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> = mutableSaveSuccess

    private val mutableError = MutableLiveData<AppError>()
    val error: LiveData<AppError> = mutableError

    fun find(id: Long) {
        useCase.findMovie(id)
                .subscribeBy(
                        onSuccess = {
                            mutableMovie.postValue(it)
                        },
                        onError = {
                            mutableError.postValue(AppError(it, "映画情報の編集画面 取得"))
                        }
                ).addTo(compositeDisposable)
    }

    fun save(movie: Movie) {
        useCase.saveWithRemote(movie)
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = {
                            mutableSaveSuccess.postValue(true)
                        },
                        onError = {
                            mutableError.postValue(AppError(it, "映画情報の編集画面 保存"))
                        }
                ).addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}