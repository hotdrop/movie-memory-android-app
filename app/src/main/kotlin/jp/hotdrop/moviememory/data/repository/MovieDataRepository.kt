package jp.hotdrop.moviememory.data.repository

import io.reactivex.Flowable
import jp.hotdrop.moviememory.data.remote.MovieApi
import jp.hotdrop.moviememory.data.remote.mapper.toComingSoonMovies
import jp.hotdrop.moviememory.model.Movie
import javax.inject.Inject

class MovieDataRepository @Inject constructor(
        private val api: MovieApi
): MovieRepository {

    /**
     * TODO MovieResponse直下の値は今完全に無視している
     */
    override fun loadNowPlayingMovies(index: Int, offset: Int): Flowable<List<Movie>> {
//        return api.getNowPraying(index, offset)
//                .map { it.toNowPlayingMovies() }
        val list = (1..10).map { Movie(it, "テスト$it", "概要", Movie.Status.NowPlaying) }
        return Flowable.fromArray(list)
    }

    override fun loadComingSoonMovies(index: Int, offset: Int): Flowable<List<Movie>> =
            api.getComingSoon(index, offset)
                .map { it.toComingSoonMovies() }
}