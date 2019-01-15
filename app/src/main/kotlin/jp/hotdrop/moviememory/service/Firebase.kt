package jp.hotdrop.moviememory.service

import com.google.firebase.auth.FirebaseAuth
import dagger.Reusable
import timber.log.Timber
import javax.inject.Inject

@Reusable
class Firebase @Inject constructor() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun login(loginFailureListener: () -> Unit) {
        if (auth.currentUser == null) {
            auth.signInAnonymously()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Timber.d("Firebaseのログインに成功しました。")
                        } else {
                            Timber.d("Firebaseのログインに失敗しました。")
                            loginFailureListener()
                        }
                    }
        }
    }
}