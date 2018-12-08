package jp.hotdrop.moviememory.data.local

import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.dao.MovieDao
import jp.hotdrop.moviememory.data.local.entity.CategoryEntity
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import jp.hotdrop.moviememory.model.Suggestion
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

    fun findMovies(suggestion: Suggestion): Single<List<MovieEntity>> =
            dao.selectMovies(suggestion.keyword)

    fun movieWithFlowable(id: Int): Flowable<MovieEntity> =
            dao.selectWithFlowable(id)

    fun find(id: Int): Single<MovieEntity> =
            dao.select(id)

    fun findWithDirect(id: Int): MovieEntity =
            dao.selectWithDirect(id)

    fun findRecentId(): Single<Int> =
            dao.selectRecentId()

    fun isExist(): Single<Boolean> =
            dao.count().map { it > 0 }

    /**
     * 映画情報の保存や削除
     */
    fun save(entities: List<MovieEntity>) {
        dao.insert(entities)
    }

    fun deleteAll() {
        dao.deleteAll()
    }

    fun findCategories(): Single<List<CategoryEntity>> =
            dao.selectCategories()
}