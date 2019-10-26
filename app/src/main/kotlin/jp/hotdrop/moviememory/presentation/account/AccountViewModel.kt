package jp.hotdrop.moviememory.presentation.account

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.model.AppError
import jp.hotdrop.moviememory.model.User
import jp.hotdrop.moviememory.usecase.AccountUseCase
import javax.inject.Inject

class AccountViewModel @Inject constructor(
        private val useCase: AccountUseCase
): ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    private val mutableUser = MutableLiveData<User>()
    val user: LiveData<User> = mutableUser

    private val mutableError = MutableLiveData<AppError>()
    val error: LiveData<AppError> = mutableError

    fun updateUser() {
        useCase.getUser()
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = {
                            mutableUser.postValue(it)
                        },
                        onError = {
                            mutableError.postValue(AppError(it, "アカウント ユーザー情報の取得"))
                        }
                ) .addTo(compositeDisposable)
    }

    fun loginByGoogle(idToken: String) {
        useCase.loginByGoogle(idToken)
                .subscribeBy(
                        onSuccess = {
                            mutableUser.postValue(it)
                        },
                        onError = {
                            mutableError.postValue(AppError(it, "アカウント Googleアカウントでのログイン"))
                        }
                ) .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}