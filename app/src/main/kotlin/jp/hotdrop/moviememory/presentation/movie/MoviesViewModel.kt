package jp.hotdrop.moviememory.presentation.movie

import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.model.AppError
import jp.hotdrop.moviememory.usecase.MovieUseCase
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
        private val useCase: MovieUseCase
): ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    private val mutablePrepared = MutableLiveData<Boolean>()
    val prepared: LiveData<Boolean> = mutablePrepared

    private val mutableError = MutableLiveData<AppError>()
    val error: LiveData<AppError> = mutableError

    /**
     * 映画情報データの準備をする。準備とは画面表示用のデータがあるかどうか
     * なければ後ろのレイヤーに準備してもらう。
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        useCase.prepared()
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = {
                            mutablePrepared.postValue(true)
                        },
                        onError = {
                            mutableError.postValue(AppError(it))
                        }
                ).addTo(compositeDisposable)
    }

    fun clear() {
        mutableError.postValue(null)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}