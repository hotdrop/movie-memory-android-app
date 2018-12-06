package jp.hotdrop.moviememory.presentation.search

import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.model.AppError
import jp.hotdrop.moviememory.model.SearchKeyword
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.usecase.SearchUseCase
import timber.log.Timber
import javax.inject.Inject

class SearchResultViewModel @Inject constructor(
        private val useCase: SearchUseCase
): ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    val suggestion: LiveData<List<SearchKeyword>> =
        LiveDataReactiveStreams.fromPublisher(useCase.suggestion())

    private val mutableClearSearchKeyword = MutableLiveData<Boolean>()
    val clearSearchKeyword: LiveData<Boolean> = mutableClearSearchKeyword

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
                ).addTo(compositeDisposable)
    }

    fun saveKeyword(searchKeyword: SearchKeyword) {
        useCase.saveSuggestion(searchKeyword)
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = {
                            Timber.d("${searchKeyword.value} の保存に成功しました")
                        },
                        onError = {
                            Timber.d("${searchKeyword.value} の保存に失敗しました。。")
                        }
                )
    }

    fun clearKeyword() {
        useCase.deleteSuggestion()
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = {
                            mutableClearSearchKeyword.postValue(true)
                        },
                        onError = {
                            mutableClearSearchKeyword.postValue(false)
                        }
                ).addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}