package jp.hotdrop.moviememory.data.repository

import io.reactivex.Flowable
import jp.hotdrop.moviememory.data.remote.MovieApi
import jp.hotdrop.moviememory.data.remote.mapper.toComingSoonMovies
import jp.hotdrop.moviememory.data.remote.mapper.toNowPlayingMovies
import jp.hotdrop.moviememory.model.Movie
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class MovieDataRepository @Inject constructor(
        private val api: MovieApi
): MovieRepository {

    /**
     * TODO MovieResponse直下の値は今完全に無視している
     */
    override fun loadNowPlayingMovies(index: Int, offset: Int, hogeFlagTest: Boolean): Flowable<List<Movie>> {
        return if (hogeFlagTest) {
            // TODO APIできてないからつなぎ
            val list = (1..10).map { createTestMovie(it) }
            Flowable.fromArray(list)
        } else {
            api.getNowPraying(index, offset).map { it.toNowPlayingMovies() }
        }
    }

    override fun loadComingSoonMovies(index: Int, offset: Int): Flowable<List<Movie>> =
            api.getComingSoon(index, offset)
                .map { it.toComingSoonMovies() }

    private fun createTestMovie(id: Int) =
        Movie(id, "テスト$id",
                "概要",
                "https://test.test",
                LocalDate.of(2018, 4, 20),
                "監督です。",
                "https://www.google.co.jp",
                "https://www.youtube.test",
                LocalDateTime.now(),
                false,
                LocalDate.of(2018, 5, 1),
                "メモです。",
                Movie.Status.NowPlaying)
}