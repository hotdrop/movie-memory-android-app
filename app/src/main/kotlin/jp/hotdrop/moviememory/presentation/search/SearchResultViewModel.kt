package jp.hotdrop.moviememory.presentation.search

import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.model.*
import jp.hotdrop.moviememory.usecase.MovieUseCase
import jp.hotdrop.moviememory.usecase.SearchUseCase
import timber.log.Timber
import javax.inject.Inject

class SearchResultViewModel @Inject constructor(
        private val searchUseCase: SearchUseCase,
        private val movieUseCase: MovieUseCase
): ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    val suggestion: LiveData<List<Suggestion>> =
        LiveDataReactiveStreams.fromPublisher(searchUseCase.suggestion())

    private val mutablePrepared = MutableLiveData<Boolean>()
    val prepared: LiveData<Boolean> = mutablePrepared

    private val mutableClearedSuggestions= MutableLiveData<Boolean>()
    val clearedSuggestions: LiveData<Boolean> = mutableClearedSuggestions

    private val mutableMovies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = mutableMovies

    private val mutableRefreshMovie = MutableLiveData<Movie>()
    val refreshMovie: LiveData<Movie> = mutableRefreshMovie

    private val mutableError = MutableLiveData<AppError>()
    val error: LiveData<AppError> = mutableError

    private var originalMoviesWithCondition: List<Movie>? = null

    fun prepared(condition: SearchCondition) {
        when (condition) {
            is SearchCondition.Keyword -> {
                mutablePrepared.postValue(true)
            }
            else -> {
                searchUseCase.find(condition)
                        .observeOn(Schedulers.io())
                        .subscribeBy(
                                onSuccess = {
                                    if (it.isEmpty()) {
                                        mutablePrepared.postValue(false)
                                    } else {
                                        originalMoviesWithCondition = it
                                        mutableMovies.postValue(it)
                                        mutablePrepared.postValue(true)
                                    }
                                },
                                onError = {
                                    mutableError.postValue(AppError(throwable = it))
                                }
                        ).addTo(compositeDisposable)
            }
        }
    }

    /**
     * 検索条件がカテゴリーやお気に入りの場合、preparedで初回検索時の映画情報リストを持っている。
     * キーワード検索はこれらに対して行えるためRoom経由で取ってしまうと最初の検索と整合性が取れなくなる。
     * 従って、最初から検索条件があるものはオリジナルの映画情報リストで検索する
     */
    fun find(query: String) {
        val keyword = SearchCondition.Keyword(query)
        if (originalMoviesWithCondition == null) {
            findFromUseCase(keyword)
        } else {
            findFromFieldVariable(keyword)
        }
    }

    private fun findFromUseCase(keyword: SearchCondition.Keyword) {
        searchUseCase.find(keyword)
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

    private fun findFromFieldVariable(keyword: SearchCondition.Keyword) {
        originalMoviesWithCondition?.let {
            val moviesWithFilter = it.filter { movie -> keyword.condition(movie) }
            mutableMovies.postValue(moviesWithFilter)
        }
    }

    fun save(query: String) {
        val suggestion = suggestion.value?.find {
            it.keyword == query.trim()
        } ?: Suggestion(keyword = query)
        searchUseCase.save(suggestion)
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
        searchUseCase.deleteSuggestions()
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

    fun onRefreshMovie(id: Long) {
        movieUseCase.findMovie(id)
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = {
                            mutableRefreshMovie.postValue(it)
                        },
                        onError = {
                            mutableError.postValue(AppError(it))
                        }
                )
                .addTo(compositeDisposable)
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