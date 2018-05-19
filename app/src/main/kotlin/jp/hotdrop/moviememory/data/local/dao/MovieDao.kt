package jp.hotdrop.moviememory.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import jp.hotdrop.moviememory.data.local.entity.LocalMovieInfoEntity
import jp.hotdrop.moviememory.data.local.entity.MovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie WHERE playingDate BETWEEN :startAt AND :endAt")
    fun getMovies(startAt: Long, endAt: Long): Flowable<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<MovieEntity>)

    @Query("SELECT * FROM movie_local_info WHERE id = :id")
    fun getLocalMovieInfo(id: Int): LocalMovieInfoEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalMovieInfo(entities: LocalMovieInfoEntity)
}