package jp.hotdrop.moviememory.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import jp.hotdrop.moviememory.data.local.entity.SuggestionEntity

@Dao
interface SuggestionDao {

    @Query("SELECT * FROM suggestion ORDER BY id DESC")
    fun suggestions(): Flowable<List<SuggestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: SuggestionEntity)

    @Query("DELETE FROM suggestion")
    fun delete()
}