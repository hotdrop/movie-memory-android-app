package jp.hotdrop.moviememory.data.repository

import io.reactivex.Flowable
import jp.hotdrop.moviememory.model.Movie

interface MovieRepository {
    // TODO データはDBに一度保持し、そこから持ってくるようにする
    fun loadNowPlayingMovies(index: Int = 0, offset: Int = 20): Flowable<List<Movie>>
    fun loadComingSoonMovies(index: Int = 0, offset: Int = 20): Flowable<List<Movie>>
}