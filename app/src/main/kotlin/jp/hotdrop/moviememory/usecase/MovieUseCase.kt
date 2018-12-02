package jp.hotdrop.moviememory.usecase

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.data.repository.MovieRepository
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.model.MovieType
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

class MovieUseCase @Inject constructor(
        private val repository: MovieRepository
) {

    fun prepared(): Completable =
            repository.prepared()
                    .subscribeOn(Schedulers.io())

    fun findMovies(type: MovieType, index: Int, offset: Int): Single<List<Movie>> {
        return when (type) {
            MovieType.NowPlaying -> findNowPlayingMovies(index, offset)
            MovieType.ComingSoon -> findComingSoonMovies(index, offset)
            MovieType.Past -> findPastMovies(index, offset)
        }
    }

    private fun findNowPlayingMovies(index: Int, offset: Int): Single<List<Movie>> {
        Timber.d("公開中のデータを取得します。")
        val endAt = LocalDate.now()
        val startAt = endAt.minusMonths(NOW_PLAYING_BETWEEN_MONTH)
        return repository.findNowPlayingMovies(startAt, endAt, index, offset)
                .subscribeOn(Schedulers.io())
    }

    private fun findComingSoonMovies(startIndex: Int, offset: Int): Single<List<Movie>> {
        Timber.d("公開予定のデータを取得します。")
        val startAt = LocalDate.now()
        return repository.findComingSoonMovies(startAt, startIndex, offset)
                .subscribeOn(Schedulers.io())
    }

    private fun findPastMovies(startIndex: Int, offset: Int): Single<List<Movie>> {
        Timber.d("公開終了のデータを取得します。")
        val startAt = LocalDate.now().minusMonths(NOW_PLAYING_BETWEEN_MONTH)
        return repository.findPastMovies(startAt, startIndex, offset)
                .subscribeOn(Schedulers.io())
    }

    fun clearMovies(): Completable =
            repository.clearMovies()
                    .subscribeOn(Schedulers.io())

    fun loadRecentMovies(): Completable =
            repository.loadRecentMovies()
                    .subscribeOn(Schedulers.io())

    fun movieFlowable(id: Int): Flowable<Movie> =
            repository.movieFlowable(id)
                    .subscribeOn(Schedulers.io())

    fun findMovie(id: Int): Single<Movie> =
            repository.findMovie(id)
                    .subscribeOn(Schedulers.io())

    fun saveLocalEdit(movie: Movie): Completable =
            repository.saveLocalMovieInfo(movie)
                    .subscribeOn(Schedulers.io())

    companion object {
        private const val NOW_PLAYING_BETWEEN_MONTH = 2L
    }
}