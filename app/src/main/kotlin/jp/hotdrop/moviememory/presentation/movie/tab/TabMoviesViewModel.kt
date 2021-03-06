package jp.hotdrop.moviememory.presentation.movie.tab

import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.model.AppError
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.model.MovieCondition
import jp.hotdrop.moviememory.usecase.MovieUseCase
import javax.inject.Inject

class TabMoviesViewModel @Inject constructor(
        private val useCase: MovieUseCase
): ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    private val mutableMovies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = mutableMovies

    private val mutableRefreshMovie = MutableLiveData<Movie>()
    val refreshMovie: LiveData<Movie> = mutableRefreshMovie

    private val mutableError = MutableLiveData<AppError>()
    val error: LiveData<AppError> = mutableError

    var condition: MovieCondition? = null

    /**
     * 初回ロード時に実行
     * LiveDataにデータが溜まっている場合は何もしない
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        movies.value?.size?.let {
            if (it > 0) {
                return
            }
        }
        onLoad(0)
    }

    /**
     * 上にスワイプして最新情報を取得する場合に使う
     */
    fun onRefresh() {
        useCase.loadRecentMovies()
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = {
                            onLoad(0)
                        },
                        onError = {
                            mutableError.postValue(AppError(it))
                        }
                ).addTo(compositeDisposable)
    }

    fun onLoad(page: Int) {
        condition?.let { type ->
            val index = page * OFFSET
            useCase.findMovies(type, index, OFFSET)
                    .observeOn(Schedulers.io())
                    .subscribeBy(
                            onSuccess = {
                                mutableMovies.postValue(it)
                            },
                            onError = {
                                mutableError.postValue(AppError(it))
                            }
                    ).addTo(compositeDisposable)
        } ?: IllegalStateException("typeがnullです。プログラムを見直してください。")
    }

    fun onRefreshMovie(id: Long) {
        useCase.findMovie(id)
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
        mutableRefreshMovie.postValue(null)
        mutableError.postValue(null)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    companion object {
        const val OFFSET = 100
    }
}