package jp.hotdrop.moviememory.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.entity.MovieNoteEntity
import jp.hotdrop.moviememory.model.SearchCondition

@Dao
interface MovieNoteDao {

    @Query("SELECT * FROM movie_note WHERE id = :id")
    fun select(id: Int): MovieNoteEntity

    @Query("SELECT * FROM movie_note WHERE ${SearchCondition.LIKE_KEYWORD_BY_LOCAL}")
    fun select(keyword: String): Single<List<MovieNoteEntity>>

    @Query("SELECT * FROM movie_note WHERE favoriteCount >= :favoriteNum")
    fun selectMoreThan(favoriteNum: Int): Single<List<MovieNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: MovieNoteEntity)
}