package jp.hotdrop.moviememory.data.local.dao

import androidx.room.*
import io.reactivex.Flowable
import jp.hotdrop.moviememory.data.local.entity.SuggestionEntity

@Dao
interface SuggestionDao {

    @Query("SELECT * FROM suggestion ORDER BY createAt DESC")
    fun suggestions(): Flowable<List<SuggestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: SuggestionEntity)

    @Delete
    fun delete(entity: SuggestionEntity)

    @Query("DELETE FROM suggestion")
    fun delete()
}