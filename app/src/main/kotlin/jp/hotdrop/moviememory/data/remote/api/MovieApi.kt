package jp.hotdrop.moviememory.data.remote.api

import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.data.remote.response.MovieResponse
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.service.Firebase
import javax.inject.Inject

@Reusable
class MovieApi @Inject constructor(
        private val firestore: Firebase
) {

    fun findAll(): Single<List<MovieResponse>> {
        return firestore.getDocuments()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
    }

    fun find(fromCreatedAt: Long): Single<List<MovieResponse>> {
        return firestore.getDocument(fromCreatedAt)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
    }

    fun save(movie: Movie): Completable {
        return firestore.saveDocument(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
    }
}