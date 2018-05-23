package jp.hotdrop.moviememory.data.local

import android.arch.persistence.room.RoomDatabase
import io.reactivex.Flowable
import jp.hotdrop.moviememory.data.local.dao.MovieDao
import jp.hotdrop.moviememory.data.local.entity.LocalMovieInfoEntity
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import org.threeten.bp.LocalDate
import javax.inject.Inject

class MovieDatabase @Inject constructor(
        private val database: RoomDatabase,
        private val dao: MovieDao
) {

    /**
     * 公開日から2ヶ月以内の映画情報を取得する
     */
    fun getNowPlayingMovies(): Flowable<List<MovieEntity>> {
        val endAt = LocalDate.now()
        val startAt = endAt.minusMonths(2L)
        return dao.getMovies(startAt.toEpochDay(), endAt.toEpochDay())
    }

    fun getLocalMovieInfo(id: Int): LocalMovieInfoEntity =
            dao.getLocalMovieInfo(id)

    fun save(entities: List<MovieEntity>) {
        database.runInTransaction {
            dao.insert(entities)
        }
    }

    fun refresh(entities: List<MovieEntity>) {
        database.runInTransaction {
            dao.clearAndInsert(entities)
        }
    }

    fun saveLocalInfo(entity: LocalMovieInfoEntity) {
        database.runInTransaction {
            dao.insertLocalMovieInfo(entity)
        }
    }
}