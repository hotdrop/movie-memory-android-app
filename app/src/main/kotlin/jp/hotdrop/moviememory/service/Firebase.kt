package jp.hotdrop.moviememory.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.remote.response.MovieResponse
import jp.hotdrop.moviememory.data.remote.response.toResponse
import jp.hotdrop.moviememory.model.AppDate
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.model.User
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

    fun loginByAnonymous(onFailure: () -> Unit) {
        if (auth.currentUser == null) {
            auth.signInAnonymously()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Timber.d("Firebaseのログインに成功しました。")
                        } else {
                            Timber.d("Firebaseのログインに失敗しました。")
                            onFailure()
                        }
                    }
        }
    }

    fun loginByGoogle(idToken: String): Completable {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return Completable.create { emitter ->
            auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Timber.d("Googleアカウント経由の認証でFirebaseへのログインに成功しました。")
                            emitter.onComplete()
                        } else {
                            Timber.e("Googleアカウント経由の認証でFirebaseへのログインに失敗しました。")
                            emitter.onError(IllegalStateException("Googleアカウント経由の認証でFirebaseへのログインに失敗"))
                        }
                    }
        }
    }

    fun getUserInfo(): User {
        return auth.currentUser?.let { firebaseUser ->
            User(id = firebaseUser.uid,
                    name = firebaseUser.displayName,
                    emailAddress = firebaseUser.email,
                    isAnonymous = firebaseUser.isAnonymous
            )
        } ?: User()
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

    /**
     * この機能は匿名ユーザーログインでは実行しないこと。
     * Firestoreのルールで縛っているため。
     */
    fun saveDocument(movie: Movie): Completable {
        return Completable.create { emitter ->
            db.collection(MOVIE_COLLECTION_NAME)
                    .document(movie.id.toString())
                    .set(movie.toMap())
                    .addOnSuccessListener {
                        emitter.onComplete()
                    }.addOnFailureListener {
                        emitter.onError(it)
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
            AppDate.toEpochFormatHyphen(remoteUpdateDateStr)
        } ?: run {
            throw IllegalArgumentException("Firestoreから取得したDateの形式が不正です。lastDate=$remoteUpdateDateStr")
        }
        return lastLocalUpdateDateEpoch < remoteUpdateDateEpoch
    }
}