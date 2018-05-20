package jp.hotdrop.moviememory.presentation.movie.nowplaying

import android.arch.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import jp.hotdrop.moviememory.domain.MoviesUseCase
import jp.hotdrop.moviememory.model.Movie
import timber.log.Timber
import javax.inject.Inject

class NowPlayingMoviesViewModel @Inject constructor(
        private val useCase: MoviesUseCase
): ViewModel(), LifecycleObserver {

    private lateinit var compositeDisposable: CompositeDisposable
    private var currentIndex = 0
    private val offset = 20

    val movies: LiveData<List<Movie>> by lazy {
        LiveDataReactiveStreams.fromPublisher(useCase.nowPlayingMovies(currentIndex, offset))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        compositeDisposable = CompositeDisposable()
    }

    /**
     * Activityが破棄された時に呼ばれる
     */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun onRefresh() {
        refresh(currentIndex)
    }

    /**
     * indexを指定したデータの更新と取得を行う。
     * 主にViewerを下までスクロールして続きを取得する場合、または上にスワイプして最新情報を取得する場合に使う。
     */
    private fun refresh(index: Int) {
        useCase.refreshNowPlayingMovies(index, offset)
                .subscribeBy(
                        onComplete = { /* TODO 更新した旨のtoast表示 */ },
                        onError = { Timber.e(it) }
                )
                .addTo(compositeDisposable)
    }

    /**
     * データが取得できなかった場合はこのリトライつきメソッドで再度取得を試みる。
     * それでもダメならToast出して終わる
     */
    fun onRefreshWithRetry() {
        useCase.refreshNowPlayingMovies(currentIndex, offset)
                .retry(3)
                .subscribeBy(
                        onComplete = { /* TODO 更新した旨のtoast表示 */ },
                        onError = {
                            Timber.e(it)
                        }
                )
                .addTo(compositeDisposable)
    }
}