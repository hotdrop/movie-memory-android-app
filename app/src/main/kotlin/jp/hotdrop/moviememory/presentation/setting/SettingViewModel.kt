package jp.hotdrop.moviememory.presentation.setting

import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.model.AppError
import jp.hotdrop.moviememory.usecase.MovieUseCase
import javax.inject.Inject

class SettingViewModel @Inject constructor(
        private val useCase: MovieUseCase
): ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    private val mutableMovieCount = MutableLiveData<Long>()
    val movieCount: LiveData<Long> = mutableMovieCount

    private val mutableLastUpdateDateEpoch = MutableLiveData<Long>()
    val lastUpdateDateEpoch: LiveData<Long> = mutableLastUpdateDateEpoch

    private val mutableClearedMovies = MutableLiveData<Boolean>()
    val clearedMovies: LiveData<Boolean> = mutableClearedMovies

    private val mutableError = MutableLiveData<AppError>()
    val error: LiveData<AppError> = mutableError

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        useCase.count()
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = {
                            mutableMovieCount.postValue(it)
                        },
                        onError = {
                            mutableError.postValue(AppError(it, "アプリ設定のデータ数カウント"))
                        }
                ).addTo(compositeDisposable)
        useCase.findDateOfGetMovieFromRemote()
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = {
                            mutableLastUpdateDateEpoch.postValue(it)
                        },
                        onError = {
                            mutableError.postValue(AppError(it, "アプリ設定の「映画情報をサーバーから取得した日」の取得"))
                        }
                ).addTo(compositeDisposable)
    }

    fun clearMovies() {
        useCase.clearMovies()
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = {
                            mutableClearedMovies.postValue(true)
                        },
                        onError = {
                            mutableError.postValue(AppError(it, "アプリ設定のクリア処理"))
                        }
                ).addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}