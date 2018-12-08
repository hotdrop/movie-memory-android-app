package jp.hotdrop.moviememory.data.local.dao

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.entity.CategoryEntity
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import jp.hotdrop.moviememory.model.Suggestion

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie WHERE playingDate BETWEEN :startAt AND :endAt ORDER BY playingDate DESC")
    fun selectMoviesByBetween(startAt: Long, endAt: Long): Single<List<MovieEntity>>

    @Query("SELECT * FROM movie WHERE playingDate > :startAt ORDER BY playingDate ASC")
    fun selectMoviesByAfter(startAt: Long): Single<List<MovieEntity>>

    @Query("SELECT * FROM movie WHERE playingDate < :startAt ORDER BY playingDate DESC")
    fun selectMoviesByBefore(startAt: Long): Single<List<MovieEntity>>

    @Query("SELECT * FROM movie WHERE ${Suggestion.QUERY}")
    fun selectMovies(keyword: String): Single<List<MovieEntity>>

    @Query("SELECT COUNT(*) FROM movie")
    fun count(): Single<Long>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun selectWithFlowable(id: Int): Flowable<MovieEntity>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun select(id: Int): Single<MovieEntity>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun selectWithDirect(id: Int): MovieEntity

    @Query("SELECT max(id) FROM movie")
    fun selectRecentId(): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<MovieEntity>)

    @Query("DELETE FROM movie")
    fun deleteAll()

    @Query("SELECT DISTINCT categoryId, categoryName FROM movie")
    fun selectCategories(): Single<List<CategoryEntity>>
}