package jp.hotdrop.moviememory.presentation.search

import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.model.AppError
import jp.hotdrop.moviememory.model.Suggestion
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.usecase.SearchUseCase
import timber.log.Timber
import javax.inject.Inject

class SearchResultViewModel @Inject constructor(
        private val useCase: SearchUseCase
): ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    val suggestion: LiveData<List<Suggestion>> =
        LiveDataReactiveStreams.fromPublisher(useCase.suggestion())

    private val mutableClearedSuggestions= MutableLiveData<Boolean>()
    val clearedSuggestions: LiveData<Boolean> = mutableClearedSuggestions

    private val mutableMovies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = mutableMovies

    private val mutableError = MutableLiveData<AppError>()
    val error: LiveData<AppError> = mutableError

    fun find(query: String) {
        useCase.findByKeyword(Suggestion(keyword = query))
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

    fun save(query: String) {
        val suggestion = suggestion.value?.find {
            it.keyword == query.trim()
        } ?: Suggestion(keyword = query)
        useCase.save(suggestion)
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = {
                            Timber.d("$query の保存に成功しました")
                        },
                        onError = {
                            Timber.d("$query の保存に失敗しました。。")
                        }
                )
    }

    fun clearSuggestions() {
        useCase.deleteSuggestions()
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = {
                            mutableClearedSuggestions.postValue(true)
                        },
                        onError = {
                            mutableClearedSuggestions.postValue(false)
                        }
                ).addTo(compositeDisposable)
    }

    fun clear() {
        mutableClearedSuggestions.postValue(null)
        mutableError.postValue(null)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}