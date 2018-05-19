package jp.hotdrop.moviememory.domain

import io.reactivex.Flowable
import jp.hotdrop.moviememory.data.repository.MovieRepository
import jp.hotdrop.moviememory.model.Movie
import javax.inject.Inject

class MoviesUseCase @Inject constructor(
        private val repository: MovieRepository
) {

    /**
     * ローカルからデータを取得する
     * データがなければネットワークから取得する
     */
    fun nowPlayingMovies(): Flowable<List<Movie>> {
        return Flowable.fromArray()
    }
}