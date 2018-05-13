package jp.hotdrop.moviememory.data.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.data.local.MovieDatabase
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import jp.hotdrop.moviememory.data.local.entity.toNowPlayingMovie
import jp.hotdrop.moviememory.data.remote.MovieApi
import jp.hotdrop.moviememory.model.Movie
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import timber.log.Timber
import javax.inject.Inject

class MovieDataRepository @Inject constructor(
        private val api: MovieApi,
        private val movieDatabase: MovieDatabase
): MovieRepository {

    override fun loadNowPlayingMovies(index: Int, offset: Int): Completable {
        return Completable.create {
            api.getNowPraying(index, offset).map { movieEntities ->
                movieEntities.forEach {
                    Timber.d("  Get movie data from api. title=${it.title}")
                }
                movieDatabase.save(movieEntities)
            }.subscribeOn(Schedulers.io())
        }
    }

    override fun nowPlayingMovies(index: Int, offset: Int, hogeFlagTest: Boolean): Flowable<List<Movie>> {
        return if (hogeFlagTest) {
            // いずれ消す
            Flowable.fromArray((1..10).map {index ->
                createTestMovieEntity(index)
            }.map { entity ->
                val localMovieInfo = movieDatabase.getLocalMovieInfo(entity.id)
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

    override fun loadComingSoonMovies(index: Int, offset: Int): Flowable<List<Movie>> {
        TODO("未実装")
    }

    private fun createTestMovieEntity(id: Int) =
        MovieEntity(id,
                "テスト$id",
                "概要",
                "https://test.test",
                LocalDateTime.parse("2018-05-02").toInstant(ZoneOffset.UTC),
                "監督です。",
                "https://www.google.co.jp",
                "https://www.youtube.test",
                LocalDateTime.now().toInstant(ZoneOffset.UTC))
}