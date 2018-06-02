package jp.hotdrop.moviememory.data.local.dao

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.entity.LocalMovieInfoEntity
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import org.threeten.bp.LocalDate

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie WHERE playingDate BETWEEN :startAt AND :endAt Order by playingDate DESC")
    fun getMovies(startAt: Long, endAt: Long): Flowable<List<MovieEntity>>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getMovie(id: Int): Single<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<MovieEntity>)

    @Query("DELETE FROM movie")
    fun deleteAll()

    @Query("DELETE FROM movie WHERE playingDate BETWEEN :startAt AND :endAt")
    fun deleteRange(startAt: Long, endAt: Long)

    @Query("SELECT * FROM movie_local_info WHERE id = :id")
    fun getLocalMovieInfo(id: Int): LocalMovieInfoEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalMovieInfo(entities: LocalMovieInfoEntity)

    @Transaction
    fun clearAndInsert(movies: List<MovieEntity>, startAt: LocalDate?, endAt: LocalDate?) {
        if (startAt != null && endAt != null) {
            deleteRange(startAt.toEpochDay(), endAt.toEpochDay())
        } else {
            deleteAll()
        }
        insert(movies)
    }
}