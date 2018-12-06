package jp.hotdrop.moviememory.data.local.dao

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.entity.CategoryEntity
import jp.hotdrop.moviememory.data.local.entity.LocalMovieInfoEntity
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import jp.hotdrop.moviememory.model.SearchKeyword

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie WHERE playingDate BETWEEN :startAt AND :endAt ORDER BY playingDate DESC")
    fun selectMoviesByBetween(startAt: Long, endAt: Long): Single<List<MovieEntity>>

    @Query("SELECT * FROM movie WHERE playingDate > :startAt ORDER BY playingDate ASC")
    fun selectMoviesByAfter(startAt: Long): Single<List<MovieEntity>>

    @Query("SELECT * FROM movie WHERE playingDate < :startAt ORDER BY playingDate DESC")
    fun selectMoviesByBefore(startAt: Long): Single<List<MovieEntity>>

    @Query("SELECT * FROM movie WHERE ${SearchKeyword.QUERY} ")
    fun selectMovies(searchKeyword: SearchKeyword): Single<List<MovieEntity>>

    @Query("SELECT COUNT(*) FROM movie")
    fun count(): Single<Long>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun selectMovieFlowable(id: Int): Flowable<MovieEntity>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun selectMovie(id: Int): Single<MovieEntity>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun selectMovieWithDirect(id: Int): MovieEntity

    @Query("SELECT max(id) FROM movie")
    fun selectRecentMovieId(): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movies: List<MovieEntity>)

    @Query("DELETE FROM movie")
    fun deleteAll()

    // ここからMovieInfo

    @Query("SELECT * FROM movie_local_info WHERE id = :id")
    fun selectLocalMovieInfo(id: Int): LocalMovieInfoEntity

    @Query("SELECT * FROM movie_local_info WHERE ${SearchKeyword.LOCAL_INFO_QUERY}")
    fun selectLocalMovieInfo(keyword: SearchKeyword): Single<List<LocalMovieInfoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalMovieInfo(entities: LocalMovieInfoEntity)

    @Transaction
    fun clearAndInsert(movies: List<MovieEntity>) {
        deleteAll()
        insert(movies)
    }

    @Query("SELECT DISTINCT categoryId, categoryName FROM movie")
    fun selectCategories(): Single<List<CategoryEntity>>
}