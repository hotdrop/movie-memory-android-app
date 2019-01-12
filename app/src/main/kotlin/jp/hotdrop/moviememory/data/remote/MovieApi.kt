package jp.hotdrop.moviememory.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.data.remote.response.MovieResponse
import jp.hotdrop.moviememory.data.remote.response.toResponse
import timber.log.Timber
import javax.inject.Inject

/**
 * 本当は中はFirestoreSDKでデータを取得しているが、後のことを考えるとFirestore依存はこのクラスだけにしておきたい。
 * そのため、汎用的なクラス名にして隠蔽する。
 */
class MovieApi @Inject constructor() {

    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun findAll(): Single<List<MovieResponse>> {
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
        }.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
    }

    fun find(fromCreatedAt: Long): Single<List<MovieResponse>> {
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
        }.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
    }
}