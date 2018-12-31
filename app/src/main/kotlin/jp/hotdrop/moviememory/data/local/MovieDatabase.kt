package jp.hotdrop.moviememory.data.local

import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.dao.MovieDao
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import org.threeten.bp.LocalDate
import javax.inject.Inject

class MovieDatabase @Inject constructor(
        private val dao: MovieDao
) {
    fun findMoviesByBetween(startAt: LocalDate, endAt: LocalDate): Single<List<MovieEntity>> =
            dao.selectMoviesByBetween(startAt.toEpochDay(), endAt.toEpochDay())

    fun findMoviesByAfter(startAt: LocalDate): Single<List<MovieEntity>> =
            dao.selectMoviesByAfter(startAt.toEpochDay())

    fun findMoviesByBefore(startAt: LocalDate): Single<List<MovieEntity>> =
            dao.selectMoviesByBefore(startAt.toEpochDay())

    fun findMovies(keyword: String): Single<List<MovieEntity>> =
            dao.selectMovies(keyword)

    fun findMovies(categoryId: Long): Single<List<MovieEntity>> =
            dao.selectMovies(categoryId)

    fun movieWithFlowable(id: Long): Flowable<MovieEntity> =
            dao.selectWithFlowable(id)

    fun find(id: Long): Single<MovieEntity> =
            dao.select(id)

    fun findWithDirect(id: Long): MovieEntity =
            dao.selectWithDirect(id)

    fun isExist(): Single<Boolean> =
            dao.count().map { it > 0 }

    fun save(entities: List<MovieEntity>) {
        dao.insert(entities)
    }

    fun saveMovie(entity: MovieEntity) {
        dao.insert(entity)
    }

    fun deleteAll() {
        dao.deleteAll()
    }
}