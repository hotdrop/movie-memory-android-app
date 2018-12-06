package jp.hotdrop.moviememory.presentation.search

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.model.AppError
import jp.hotdrop.moviememory.model.SearchKeyword
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.usecase.SearchUseCase
import javax.inject.Inject

class SearchResultViewModel @Inject constructor(
        private val useCase: SearchUseCase
): ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    private val mutableMovies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = mutableMovies

    private val mutableError = MutableLiveData<AppError>()
    val error: LiveData<AppError> = mutableError

    fun findByKeyword(searchKeyword: SearchKeyword) {
        useCase.findByKeyword(searchKeyword)
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = {
                            mutableMovies.postValue(it)
                        },
                        onError = {
                            mutableError.postValue(AppError(throwable = it))
                        }
                )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}