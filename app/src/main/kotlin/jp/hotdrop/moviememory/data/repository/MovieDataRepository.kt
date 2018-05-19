package jp.hotdrop.moviememory.data.repository

import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.data.local.MovieDatabase
import jp.hotdrop.moviememory.data.local.entity.LocalMovieInfoEntity
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import jp.hotdrop.moviememory.data.local.entity.toNowPlayingMovie
import jp.hotdrop.moviememory.data.remote.MovieApi
import jp.hotdrop.moviememory.data.remote.response.toMovieEntity
import jp.hotdrop.moviememory.model.Movie
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import timber.log.Timber
import javax.inject.Inject

class MovieDataRepository @Inject constructor(
        private val api: MovieApi,
        private val movieDatabase: MovieDatabase
): MovieRepository {

    /**
     * ネットワークからデータを取得する
     */
    override fun loadNowPlayingMovies(index: Int, offset: Int) =
            api.getNowPraying(index, offset)
                    .doOnSuccess { movieResults ->
                        movieResults.forEach {
                            Timber.d("  Get movie data from api. title=${it.title}")
                        }
                        val movieEntities = movieResults.map { it.toMovieEntity() }
                        movieDatabase.save(movieEntities)
                    }.toCompletable()

    /**
     * LocalDBに保存する
     */
    fun save() {

    }

    /**
     * ローカルDBに保存されているデータを取得する
     */
    override fun nowPlayingMovies(index: Int, offset: Int, hogeFlagTest: Boolean): Flowable<List<Movie>> {
        return if (hogeFlagTest) {
            // TODO DBから取得できたつもり
            Flowable.fromArray((1..10).map {index ->
                createTestMovieEntity(index)
            }.map { entity ->
                val localMovieInfo = createLocalMovieInfoEntity(entity.id)
                entity.toNowPlayingMovie(localMovieInfo)
            })
        } else {
            movieDatabase.getNowPlayingMovies()
                    .map { movieEntities ->
                        movieEntities.map { entity ->
                            val localMovieInfo = movieDatabase.getLocalMovieInfo(entity.id)
                            entity.toNowPlayingMovie(localMovieInfo)
                        }
                    }
                    .subscribeOn(Schedulers.io())
        }
    }

//    override fun loadComingSoonMovies(index: Int, offset: Int): Flowable<List<Movie>> {
//        TODO("未実装")
//    }

    private fun createTestMovieEntity(id: Int) =
        MovieEntity(id,
                "テスト$id",
                "概要",
                "https://test.test",
                LocalDate.parse("2018-05-02").toEpochDay(),
                "監督です。",
                "https://www.google.co.jp",
                "https://www.youtube.test",
                LocalDateTime.now().toInstant(ZoneOffset.UTC))

    private fun createLocalMovieInfoEntity(id: Int) =
            LocalMovieInfoEntity(id,
                    false,
                    LocalDate.parse("2018-05-20").toEpochDay(),
                    "Memo dummy"
            )
}