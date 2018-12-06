package jp.hotdrop.moviememory.data.local.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.entity.MovieNoteEntity
import jp.hotdrop.moviememory.model.SearchKeyword

interface MovieNoteDao {

    @Query("SELECT * FROM movie_note WHERE id = :id")
    fun select(id: Int): MovieNoteEntity

    @Query("SELECT * FROM movie_note WHERE ${SearchKeyword.LOCAL_INFO_QUERY}")
    fun select(keyword: SearchKeyword): Single<List<MovieNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: MovieNoteEntity)
}