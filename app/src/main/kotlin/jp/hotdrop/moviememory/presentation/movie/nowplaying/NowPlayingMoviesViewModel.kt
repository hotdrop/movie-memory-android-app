package jp.hotdrop.moviememory.presentation.movie.nowplaying

import android.arch.lifecycle.*
import io.reactivex.android.schedulers.AndroidSchedulers
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
    private val offset = 20

    val movies: LiveData<List<Movie>> by lazy {
        LiveDataReactiveStreams.fromPublisher(useCase.nowPlayingMovies(offset))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        compositeDisposable = CompositeDisposable()
        refresh(null)
    }

    fun onRefresh(errorPredicate:() -> Unit) {
        refresh(errorPredicate)
    }

    /**
     * 初回起動時と上にスワイプして最新情報を取得する場合に使う
     */
    private fun refresh(errorPredicate:(() -> Unit)?) {
        useCase.refreshNowPlayingMovies(offset)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = { Timber.d( "公開中の映画をrefresh... onComplete") },
                        onError = {
                            if (errorPredicate != null) errorPredicate()
                            Timber.d("公開中の映画をrefresh... onError!")
                        }
                )
                .addTo(compositeDisposable)
    }

    fun onLoad(page: Int, errorPredicate:() -> Unit) {
        val index = page * offset
        Timber.i(" load start index = $index")
        load(index, errorPredicate)
    }

    /**
     * Viewerを下までスクロールして続きを取得する場合に使う
     */
    private fun load(index: Int, errorPredicate:() -> Unit) {
        useCase.loadNowPlayingMovies(index, offset)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = { Timber.d( "公開中の映画をrefresh... onComplete") },
                        onError = {
                            errorPredicate()
                            Timber.d("公開中の映画をrefresh... onError!")
                        }
                )
                .addTo(compositeDisposable)
    }

    /**
     * Activityが破棄された時に呼ばれる
     */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}