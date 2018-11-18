package jp.hotdrop.moviememory.data.local.dao

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.entity.LocalMovieInfoEntity
import jp.hotdrop.moviememory.data.local.entity.MovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie WHERE playingDate BETWEEN :startAt AND :endAt ORDER BY playingDate DESC")
    fun selectMovies(startAt: Long, endAt: Long): Single<List<MovieEntity>>

    @Query("SELECT COUNT(*) FROM movie")
    fun count(): Single<Long>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getMovie(id: Int): Single<MovieEntity>

    @Query("SELECT max(id) FROM movie")
    fun getRecentMovieId(): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<MovieEntity>)

    @Query("DELETE FROM movie")
    fun deleteAll()

    @Query("SELECT * FROM movie_local_info WHERE id = :id")
    fun getLocalMovieInfo(id: Int): LocalMovieInfoEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalMovieInfo(entities: LocalMovieInfoEntity)

    @Transaction
    fun clearAndInsert(movies: List<MovieEntity>) {
        deleteAll()
        insert(movies)
    }
}