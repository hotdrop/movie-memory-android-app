package jp.hotdrop.moviememory.data.repository

import io.reactivex.Single
import jp.hotdrop.moviememory.model.Movie

interface MovieRepository {
    // TODO データはDBに一度保持し、そこから持ってくるようにする
    fun loadNowPlayingMovies(index: Int = 0, offset: Int = 20): Single<List<Movie>>
    fun loadComingSoonMovies(index: Int = 0, offset: Int = 20): Single<List<Movie>>
}