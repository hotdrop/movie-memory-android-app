package jp.hotdrop.moviememory.usecase

import dagger.Reusable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.data.repository.AccountRepository
import jp.hotdrop.moviememory.model.User
import javax.inject.Inject

@Reusable
class AccountUseCase @Inject constructor(
        private val repository: AccountRepository
) {
    fun getUser(): Single<User> =
            repository.getUser()
                    .subscribeOn(Schedulers.io())

    fun loginByGoogle(idToken: String): Single<User> =
            repository.loginByGoogle(idToken)
                    .subscribeOn(Schedulers.io())
}