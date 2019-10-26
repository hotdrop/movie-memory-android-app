package jp.hotdrop.moviememory.data.repository

import dagger.Reusable
import io.reactivex.Single
import jp.hotdrop.moviememory.model.User
import jp.hotdrop.moviememory.service.Firebase
import javax.inject.Inject

@Reusable
class AccountRepository @Inject constructor(
        private val firebase: Firebase
) {
    fun getUser(): Single<User> {
        return Single.just(firebase.getUserInfo())
    }

    fun loginByGoogle(idToken: String): Single<User> {
        return firebase.loginByGoogle(idToken)
                .toSingle {
                    firebase.getUserInfo()
                }
    }
}