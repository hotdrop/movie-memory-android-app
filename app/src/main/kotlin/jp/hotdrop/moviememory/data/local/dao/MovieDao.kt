package jp.hotdrop.moviememory.data.local.dao

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.entity.CategoryEntity
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import jp.hotdrop.moviememory.model.SearchCondition

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie WHERE playingDate BETWEEN :startAt AND :endAt ORDER BY playingDate DESC")
    fun selectMoviesByBetween(startAt: Long, endAt: Long): Single<List<MovieEntity>>

    @Query("SELECT * FROM movie WHERE playingDate > :startAt ORDER BY playingDate ASC")
    fun selectMoviesByAfter(startAt: Long): Single<List<MovieEntity>>

    @Query("SELECT * FROM movie WHERE playingDate < :startAt ORDER BY playingDate DESC")
    fun selectMoviesByBefore(startAt: Long): Single<List<MovieEntity>>

    @Query("SELECT * FROM movie WHERE ${SearchCondition.LIKE_KEYWORD}")
    fun selectMovies(keyword: String): Single<List<MovieEntity>>

    @Query("SELECT * FROM movie WHERE categoryId = :categoryId")
    fun selectMovies(categoryId: Long): Single<List<MovieEntity>>

    @Query("SELECT COUNT(*) FROM movie")
    fun count(): Single<Long>

    @Query("SELECT COUNT(*) FROM movie WHERE categoryId = :categoryId")
    fun countCategory(categoryId: Long): Long

    @Query("SELECT * FROM movie WHERE id = :id")
    fun selectWithFlowable(id: Long): Flowable<MovieEntity>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun select(id: Long): Single<MovieEntity>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun selectWithDirect(id: Long): MovieEntity

    @Query("SELECT MAX(createdAt) FROM movie")
    fun selectMaxCreatedAt(): Single<Long>

    // アプリ側でデータを上書きできるので、サーバーから同じデータが飛んできたらスルーする
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: MovieEntity)

    @Query("UPDATE movie SET categoryId = :toCategoryId WHERE categoryId = :fromCategoryId")
    fun updateCategory(fromCategoryId: Long, toCategoryId: Long)

    @Query("DELETE FROM movie")
    fun deleteAll()
}