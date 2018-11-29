package jp.hotdrop.moviememory.presentation.movie.past

import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.model.AppError
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.presentation.movie.nowplaying.NowPlayingMoviesViewModel
import jp.hotdrop.moviememory.usecase.MovieUseCase
import timber.log.Timber
import javax.inject.Inject

class PastMoviesViewModel @Inject constructor(
        private val useCase: MovieUseCase
): ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    private val mutableMovies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = mutableMovies

    private val mutableMovie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = mutableMovie

    private val mutableError = MutableLiveData<AppError>()
    val error: LiveData<AppError> = mutableError

    /**
     * 最初に表示するNowPlayingMoviesでこれをやっているので以降は不要だが
     * この画面を初期に持ってくる可能性もあるので呼んでおく。
     * 作り的に別にどこで何回呼んでも大丈夫。Completableが返ってくるだけ。
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        useCase.prepared()
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = {
                            onLoad(0)
                        },
                        onError = {
                            mutableError.postValue(AppError(it))
                        }
                )
    }

    fun onLoad(page: Int) {
        val index = page * NowPlayingMoviesViewModel.OFFSET
        useCase.findComingSoonMovies(index, NowPlayingMoviesViewModel.OFFSET)
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = {
                            Timber.d( "過去の映画をrefresh... onComplete")
                            mutableMovies.postValue(it)
                        },
                        onError = {
                            mutableError.postValue(AppError(it))
                        }
                ).addTo(compositeDisposable)
    }

    /**
     * 上にスワイプして最新情報を取得する場合に使う
     */
    fun onRefresh() {
        useCase.loadRecentMovies()
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = {
                            Timber.d( "過去の映画をrefresh... onComplete")
                            onLoad(0)
                        },
                        onError = {
                            mutableError.postValue(AppError(it))
                        }
                ).addTo(compositeDisposable)
    }

    fun onRefreshMovie(id: Int) {
        useCase.findMovie(id)
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = {
                            Timber.d( "refresh対象のMovie情報を再取得")
                            mutableMovie.postValue(it)
                        },
                        onError = {
                            mutableError.postValue(AppError(it))
                        }
                )
                .addTo(compositeDisposable)
    }

    fun clear() {
        mutableMovie.postValue(null)
        mutableError.postValue(null)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}