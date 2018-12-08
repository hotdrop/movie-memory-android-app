package jp.hotdrop.moviememory.presentation.setting

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.usecase.MovieUseCase
import timber.log.Timber
import javax.inject.Inject

class SettingViewModel @Inject constructor(
        private val movieUseCase: MovieUseCase
): ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    private val mutableClearedMovies = MutableLiveData<Boolean>()
    val clearedMovies: LiveData<Boolean> = mutableClearedMovies

    fun clearMovies() {
        movieUseCase.clearMovies()
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = {
                            mutableClearedMovies.postValue(true)
                        },
                        onError = {
                            Timber.e("データクリアに失敗！")
                            mutableClearedMovies.postValue(false)
                        }
                ).addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}