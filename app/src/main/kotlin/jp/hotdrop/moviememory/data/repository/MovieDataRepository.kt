package jp.hotdrop.moviememory.data.repository

import io.reactivex.Single
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
    override fun loadNowPlayingMovies(index: Int, offset: Int): Single<List<Movie>> {
//        return api.getNowPraying(index, offset)
//                .map { it.toNowPlayingMovies() }
        val list = mutableListOf(
                Movie(1, "テスト1", "", Movie.Status.NowPlaying),
                Movie(2, "テスト2", "", Movie.Status.NowPlaying),
                Movie(3, "テスト3", "", Movie.Status.NowPlaying),
                Movie(4, "テスト4", "", Movie.Status.NowPlaying)
        )
        return Single.just(list)
    }

    override fun loadComingSoonMovies(index: Int, offset: Int): Single<List<Movie>> {
        return api.getComingSoon(index, offset).map { it.toComingSoonMovies() }
    }
}