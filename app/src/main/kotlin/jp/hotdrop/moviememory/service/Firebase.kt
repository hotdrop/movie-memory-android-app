package jp.hotdrop.moviememory.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Reusable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.remote.response.MovieResponse
import jp.hotdrop.moviememory.data.remote.response.toResponse
import jp.hotdrop.moviememory.util.DateParser
import timber.log.Timber
import javax.inject.Inject

@Reusable
class Firebase @Inject constructor() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    companion object {
        const val MOVIE_COLLECTION_NAME = "movies"
        const val INFO_COLLECTION_NAME = "scraping"
        const val INFO_DOCUMENT_NAME = "info"
    }

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
            db.collection(MOVIE_COLLECTION_NAME)
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
            db.collection(MOVIE_COLLECTION_NAME)
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

    fun listenStatus(lastUpdateDateEpoch: Long, action: (isUpdateData: Boolean, status: String) -> Unit) {
        val ref = db.collection(INFO_COLLECTION_NAME).document(INFO_DOCUMENT_NAME)
        ref.addSnapshotListener { snapshot, exception ->
            exception?.run {
                Timber.e(this, "Listen failure... ")
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                snapshot.data?.let { info ->
                    Timber.d("受け取ったデータ = $info")
                    val remoteUpdateAtStr = info["lastDate"] as? String
                    val status = info["status"] as? String ?: "Error"
                    action(haveUpdateData(lastUpdateDateEpoch, remoteUpdateAtStr), status)
                }
            } else {
                Timber.d("受け取ったデータはnullです。")
            }
        }
    }

    private fun haveUpdateData(lastLocalUpdateDateEpoch: Long, remoteUpdateDateStr: String?): Boolean {
        // remoteUpdateDateStrとParse結果のどちらかがnullなら例外を投げたかったのでletで書いた。
        val remoteUpdateDateEpoch = remoteUpdateDateStr?.let {
            DateParser.toEpochFormatHyphen(remoteUpdateDateStr)
        } ?: run {
            throw IllegalArgumentException("Firestoreから取得したDateの形式が不正です。lastDate=$remoteUpdateDateStr")
        }
        return lastLocalUpdateDateEpoch < remoteUpdateDateEpoch
    }
}