package jp.hotdrop.moviememory.data.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import jp.hotdrop.moviememory.data.local.MovieDatabase
import jp.hotdrop.moviememory.data.local.entity.toNowPlayingMovie
import jp.hotdrop.moviememory.data.remote.MovieApi
import jp.hotdrop.moviememory.data.remote.response.toMovieEntity
import jp.hotdrop.moviememory.model.Movie
import timber.log.Timber
import javax.inject.Inject

class MovieDataRepository @Inject constructor(
        private val api: MovieApi,
        private val movieDatabase: MovieDatabase
): MovieRepository {

    override val nowPlayingMovies: Flowable<List<Movie>> =
        movieDatabase.getNowPlayingMovies()
                .map { movieEntities ->
                    movieEntities.map { entity ->
                        val localMovieInfo = movieDatabase.getLocalMovieInfo(entity.id)
                        entity.toNowPlayingMovie(localMovieInfo)
                    }
                }
    /**
     * 公開中の映画情報を取得する
     */
    override fun loadNowPlayingMovies(index: Int, offset: Int): Completable =
            api.getNowPraying(index, offset)
                    .doOnSuccess { movieResults ->
                        movieResults.forEach {
                            Timber.d("  Get movie data from api. title=${it.title}")
                        }
                        val movieEntities = movieResults.map { it.toMovieEntity() }
                        movieDatabase.save(movieEntities)
                    }.toCompletable()
}