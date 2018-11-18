package jp.hotdrop.moviememory.data.local

import androidx.room.RoomDatabase
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.dao.MovieDao
import jp.hotdrop.moviememory.data.local.entity.LocalMovieInfoEntity
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import org.threeten.bp.LocalDate
import javax.inject.Inject

class MovieDatabase @Inject constructor(
        private val database: RoomDatabase,
        private val dao: MovieDao
) {

    fun findMovies(startAt: LocalDate, endAt: LocalDate): Single<List<MovieEntity>> =
            dao.selectMovies(startAt.toEpochDay(), endAt.toEpochDay())

    fun findMovie(id: Int): Single<MovieEntity> =
            dao.selectMovie(id)

    fun isExist(): Single<Boolean> =
            dao.count().map { it > 0 }

    fun findRecentMovieId(): Single<Int> =
            dao.selectRecentMovieId()

    fun save(entities: List<MovieEntity>) {
        database.runInTransaction {
            dao.insert(entities)
        }
    }

    fun findLocalMovieInfo(id: Int): LocalMovieInfoEntity =
            dao.selectLocalMovieInfo(id)

    fun saveLocalInfo(entity: LocalMovieInfoEntity) {
        database.runInTransaction {
            dao.insertLocalMovieInfo(entity)
        }
    }
}