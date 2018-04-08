package jp.hotdrop.moviememory.data.repository

import io.reactivex.Completable

interface MovieRepository {
    // データは必ずDBに保持してそこから持ってくるようにする。その場合、これが必要
    // 今はAPIから直接取得する
    //val nowPlayingMovies: Flowable<List<Movie>>
    //val comingSoonMovies: Flowable<List<Movie>>

    fun loadNowPlayingMovies(index: Int, offset: Int): Completable
    //fun loadComingSoonMovies(): Completable
}