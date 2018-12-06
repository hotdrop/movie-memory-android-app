package jp.hotdrop.moviememory.data.local

import androidx.room.RoomDatabase
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.dao.MovieDao
import jp.hotdrop.moviememory.data.local.entity.CategoryEntity
import jp.hotdrop.moviememory.data.local.entity.LocalMovieInfoEntity
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import jp.hotdrop.moviememory.model.SearchKeyword
import org.threeten.bp.LocalDate
import javax.inject.Inject

class MovieDatabase @Inject constructor(
        private val database: RoomDatabase,
        private val dao: MovieDao
) {

    /**
     * 映画情報のタイプ別取得メソッド
     */
    fun findMoviesByBetween(startAt: LocalDate, endAt: LocalDate): Single<List<MovieEntity>> =
            dao.selectMoviesByBetween(startAt.toEpochDay(), endAt.toEpochDay())

    fun findMoviesByAfter(startAt: LocalDate): Single<List<MovieEntity>> =
            dao.selectMoviesByAfter(startAt.toEpochDay())

    fun findMoviesByBefore(startAt: LocalDate): Single<List<MovieEntity>> =
            dao.selectMoviesByBefore(startAt.toEpochDay())

    /**
     * 映画情報の検索
     */
    fun findMovies(searchKeyword: SearchKeyword): Single<List<MovieEntity>> =
            dao.selectMovies(searchKeyword)

    /**
     * 1つの映画情報に関するメソッド
     */
    fun movieFlowable(id: Int): Flowable<MovieEntity> =
            dao.selectMovieFlowable(id)

    fun findMovie(id: Int): Single<MovieEntity> =
            dao.selectMovie(id)

    fun findMovieWithDirect(id: Int): MovieEntity =
            dao.selectMovieWithDirect(id)

    fun isExist(): Single<Boolean> =
            dao.count().map { it > 0 }

    fun findRecentMovieId(): Single<Int> =
            dao.selectRecentMovieId()

    /**
     * 映画情報の保存や削除
     */
    fun save(entities: List<MovieEntity>) {
        database.runInTransaction {
            dao.insert(entities)
        }
    }

    fun deleteMovies() {
        dao.deleteAll()
    }

    /**
     * ローカルで編集する映画情報
     */
    fun findLocalMovieInfo(id: Int): LocalMovieInfoEntity =
            dao.selectLocalMovieInfo(id)

    fun findLocalMoviesInfo(searchKeyword: SearchKeyword): Single<List<LocalMovieInfoEntity>> =
            dao.selectLocalMovieInfo(searchKeyword)

    fun saveLocalInfo(entity: LocalMovieInfoEntity) {
        database.runInTransaction {
            dao.insertLocalMovieInfo(entity)
        }
    }

    fun findCategories(): Single<List<CategoryEntity>> =
            dao.selectCategories()
}