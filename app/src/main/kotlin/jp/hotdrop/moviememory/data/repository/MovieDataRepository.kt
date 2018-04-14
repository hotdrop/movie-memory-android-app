package jp.hotdrop.moviememory.data.repository

import io.reactivex.Single
import jp.hotdrop.moviememory.data.remote.MovieApi
import jp.hotdrop.moviememory.data.remote.mapper.toComingSoonMovies
import jp.hotdrop.moviememory.data.remote.mapper.toNowPlayingMovies
import jp.hotdrop.moviememory.model.Movie
import javax.inject.Inject

class MovieDataRepository @Inject constructor(
        private val api: MovieApi
): MovieRepository {

    /**
     * TODO MovieResponse直下のあたいは今完全に無視
     */
    override fun loadNowPlayingMovies(index: Int, offset: Int): Single<List<Movie>> {
        return api.getNowPraying(index, offset)
                .map { it.toNowPlayingMovies() }
    }

    override fun loadComingSoonMovies(index: Int, offset: Int): Single<List<Movie>> {
        return api.getComingSoon(index, offset).map { it.toComingSoonMovies() }
    }
}