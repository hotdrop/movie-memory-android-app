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

    val movies: LiveData<List<Movie>> by lazy {
        LiveDataReactiveStreams.fromPublisher(useCase.nowPlayingMovies())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        compositeDisposable = CompositeDisposable()
        refresh()
    }

    /**
     * Activityが破棄された時に呼ばれる
     */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun onRefresh() {
        refresh()
    }

    private fun refresh() {
        useCase.loadNowPlayingMovies()
                .subscribeBy(
                        onComplete = { /* TODO 更新した旨のtoast表示 */ },
                        onError = { Timber.e(it) }
                )
                .addTo(compositeDisposable)
    }
}