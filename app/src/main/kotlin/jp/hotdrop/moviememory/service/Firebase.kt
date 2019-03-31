package jp.hotdrop.moviememory.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Reusable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.remote.response.MovieResponse
import jp.hotdrop.moviememory.data.remote.response.toResponse
import timber.log.Timber
import javax.inject.Inject

@Reusable
class Firebase @Inject constructor() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

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

    fun getDocuments(): Single<List<MovieResponse>> {
        return Single.create<List<MovieResponse>> { emitter ->
            db.collection("movies")
                    .get()
                    .addOnCompleteListener { task ->
                        val querySnapshot = task.result
                        if (task.isSuccessful && querySnapshot != null) {
                            emitter.onSuccess(querySnapshot.map { it.toResponse() })
                        } else {
                            Timber.e("Firestoreからのデータ取得に失敗しました。")
                            task.exception?.let {
                                emitter.onError(it)
                            } ?: throw IllegalStateException("Firestoreからのデータ取得に失敗し、exceptionもnullでした。")
                        }
                    }
        }
    }

    fun getDocument(fromCreatedAt: Long): Single<List<MovieResponse>> {
        return Single.create<List<MovieResponse>> { emitter ->
            db.collection("movies")
                    .whereGreaterThan("createdAt", fromCreatedAt)
                    .get()
                    .addOnCompleteListener { task ->
                        val querySnapshot = task.result
                        if (task.isSuccessful && querySnapshot != null) {
                            emitter.onSuccess(querySnapshot.map { it.toResponse() })
                        } else {
                            Timber.e("Firestoreからのデータ取得に失敗しました。")
                            task.exception?.let {
                                emitter.onError(it)
                            } ?: throw IllegalStateException("Firestoreからのデータ取得に失敗し、exceptionもnullでした。")
                        }
                    }
        }
    }
}