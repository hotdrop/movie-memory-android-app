package jp.hotdrop.moviememory.presentation.movie.nowplaying

import androidx.lifecycle.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.model.AppError
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.usecase.MovieUseCase
import timber.log.Timber
import javax.inject.Inject

class NowPlayingMoviesViewModel @Inject constructor(
        private val useCase: MovieUseCase
): ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    private val mutableMovies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = mutableMovies

    private val mutableRefreshMovie = MutableLiveData<Movie>()
    val refreshMovie: LiveData<Movie> = mutableRefreshMovie

    private val mutableError = MutableLiveData<AppError>()
    val error: LiveData<AppError> = mutableError

    /**
     * 初回起動時は全データを持ってきてDBに入れる
     * 2回目以降は差分データだけあれば取得する。refreshはなし
     * 設定画面にキャッシュ削除を設けてそれをやった場合だけ全データクリアする
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
        val index = page * OFFSET
        Timber.i(" load start index = $index")

        useCase.findNowPlayingMovies(index, OFFSET)
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = {
                            Timber.d( "公開中の映画をrefresh... onComplete")
                            mutableMovies.postValue(it)
                        },
                        onError = {
                            mutableError.postValue(AppError(it))
                        }
                )
                .addTo(compositeDisposable)
    }

    /**
     * 上にスワイプして最新情報を取得する場合に使う
     */
    fun onRefresh() {
        useCase.loadRecentMovies()
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = {
                            Timber.d( "公開中の映画をrefresh... onComplete")
                            onLoad(0)
                        },
                        onError = {
                            mutableError.postValue(AppError(it))
                        }
                )
                .addTo(compositeDisposable)
    }

    fun onRefreshMovie(id: Int) {
        useCase.findMovie(id)
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = {
                            Timber.d( "refresh対象のMovie情報を再取得")
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
        private const val OFFSET = 20
    }
}