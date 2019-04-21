package jp.hotdrop.moviememory.usecase

import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.data.repository.CategoryRepository
import jp.hotdrop.moviememory.data.repository.MovieRepository
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.model.MovieCondition
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

@Reusable
class MovieUseCase @Inject constructor(
        private val movieRepository: MovieRepository,
        private val categoryRepository: CategoryRepository
) {

    fun prepared(): Completable =
            movieRepository.prepared()
                    .subscribeOn(Schedulers.io())

    fun findMovies(condition: MovieCondition, index: Int, offset: Int): Single<List<Movie>> {
        return when (condition) {
            MovieCondition.NowPlaying -> findNowPlayingMovies(index, offset)
            MovieCondition.ComingSoon -> findComingSoonMovies(index, offset)
            MovieCondition.Past -> findPastMovies(index, offset)
        }
    }

    fun count(): Single<Long> =
            movieRepository.count()
                    .subscribeOn(Schedulers.io())

    private fun findNowPlayingMovies(index: Int, offset: Int): Single<List<Movie>> {
        val endAt = LocalDate.now()
        val startAt = endAt.minusMonths(NOW_PLAYING_BETWEEN_MONTH)
        Timber.d("公開中のデータを取得します。")
        return movieRepository.findNowPlayingMovies(startAt, endAt, index, offset)
                .subscribeOn(Schedulers.io())
    }

    private fun findComingSoonMovies(startIndex: Int, offset: Int): Single<List<Movie>> {
        Timber.d("公開予定のデータを取得します。")
        val startAt = LocalDate.now()
        return movieRepository.findComingSoonMovies(startAt, startIndex, offset)
                .subscribeOn(Schedulers.io())
    }

    private fun findPastMovies(startIndex: Int, offset: Int): Single<List<Movie>> {
        Timber.d("公開終了のデータを取得します。")
        val startAt = LocalDate.now().minusMonths(NOW_PLAYING_BETWEEN_MONTH)
        return movieRepository.findPastMovies(startAt, startIndex, offset)
                .subscribeOn(Schedulers.io())
    }

    fun clearMovies(): Completable =
            movieRepository.clearMovies()
                    .subscribeOn(Schedulers.io())

    fun loadRecentMovies(): Completable =
            movieRepository.loadRecentMovies()
                    .subscribeOn(Schedulers.io())

    fun movieFlowable(id: Long): Flowable<Movie> =
            movieRepository.movieFlowable(id)
                    .subscribeOn(Schedulers.io())

    fun findMovie(id: Long): Single<Movie> =
            movieRepository.findMovie(id)
                    .subscribeOn(Schedulers.io())

    fun findCategories(): Single<List<Category>> =
            categoryRepository.findAll()
                    .subscribeOn(Schedulers.io())

    fun save(movie: Movie): Completable =
            movieRepository.save(movie)
                    .subscribeOn(Schedulers.io())

    fun saveLocalEdit(movie: Movie): Completable =
            movieRepository.saveLocalMovieInfo(movie)
                    .subscribeOn(Schedulers.io())

    fun findDateOfGetMovieFromRemote(): Single<Long> =
            movieRepository.findDateOfGetMovieFromRemote()
                    .subscribeOn(Schedulers.io())

    companion object {
        private const val NOW_PLAYING_BETWEEN_MONTH = 2L
    }
}