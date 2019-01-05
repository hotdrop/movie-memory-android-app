package jp.hotdrop.moviememory.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category WHERE id = :id")
    fun select(id: Long): CategoryEntity

    @Query("SELECT * FROM category WHERE name = :name")
    fun select(name: String): CategoryEntity?

    @Query("SELECT * FROM category")
    fun selectAll(): Single<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: CategoryEntity): Long
}