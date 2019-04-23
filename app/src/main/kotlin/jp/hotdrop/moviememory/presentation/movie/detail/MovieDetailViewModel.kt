package jp.hotdrop.moviememory.presentation.movie.detail

import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.model.AppDate
import jp.hotdrop.moviememory.model.AppError
import jp.hotdrop.moviememory.usecase.MovieUseCase
import jp.hotdrop.moviememory.model.Movie
import timber.log.Timber
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
        private val useCase: MovieUseCase
): ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    var movie: LiveData<Movie>? = null

    private val mutableIsRefreshMovie = MutableLiveData<Boolean>()
    val isRefreshMovie: LiveData<Boolean> = mutableIsRefreshMovie

    private val mutableError = MutableLiveData<AppError>()
    val error: LiveData<AppError> = mutableError

    fun setUp(id: Long) {
        movie = LiveDataReactiveStreams.fromPublisher(useCase.movieFlowable(id))
    }

    fun saveFavorite(count: Int) {
        movie?.value?.let { movie ->
            movie.favoriteCount = count
            useCase.saveLocalEdit(movie)
                    .observeOn(Schedulers.io())
                    .subscribeBy(
                            onComplete = {
                                mutableIsRefreshMovie.postValue(true)
                            },
                            onError = { Timber.e(it) }
                    ).addTo(compositeDisposable)
        }
    }

    fun saveWatchDate(watchDate: AppDate) {
        movie?.value?.let { movie ->
            movie.watchDate = watchDate
            useCase.saveLocalEdit(movie)
                    .observeOn(Schedulers.io())
                    .subscribeBy(
                            onComplete = {
                                mutableIsRefreshMovie.postValue(true)
                            },
                            onError = {
                                mutableError.postValue(AppError(it, "Error! save watch date. value = $watchDate"))
                            }
                    ).addTo(compositeDisposable)
        }
    }

    fun saveWatchPlace(place: String) {
        movie?.value?.let { movie ->
            movie.watchPlace = place
            useCase.saveLocalEdit(movie)
                    .observeOn(Schedulers.io())
                    .subscribeBy(
                            onComplete = {
                                mutableIsRefreshMovie.postValue(true)
                            },
                            onError = {
                                mutableError.postValue(AppError(it, "Error! save watch place. value = $place"))
                            }
                    ).addTo(compositeDisposable)
        }
    }

    fun saveWatchNote(note: String) {
        movie?.value?.let { movie ->
            movie.note = note
            useCase.saveLocalEdit(movie)
                    .observeOn(Schedulers.io())
                    .subscribeBy(
                            onComplete = {
                                mutableIsRefreshMovie.postValue(true)
                            },
                            onError = {
                                mutableError.postValue(AppError(it, "Error! save watch note. value = $note"))
                            }
                    ).addTo(compositeDisposable)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}