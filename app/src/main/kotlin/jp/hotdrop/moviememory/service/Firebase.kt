package jp.hotdrop.moviememory.service

import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber
import javax.inject.Inject

class Firebase @Inject constructor() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private var token: String? = null

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

    fun getToken(): String? {
        return token
    }

    private fun refreshIdToken() {
        auth.currentUser?.let {
            it.getIdToken(true)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            token = task.result?.token
                        } else {
                            Timber.d("Tokenの取得に失敗しました。")
                        }
                    }
        }
    }
}