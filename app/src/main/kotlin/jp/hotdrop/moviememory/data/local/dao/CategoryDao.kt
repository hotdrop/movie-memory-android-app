package jp.hotdrop.moviememory.data.local.dao

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    fun flowable(): Flowable<List<CategoryEntity>>

    @Query("SELECT * FROM category")
    fun selectAll(): Single<List<CategoryEntity>>

    @Query("SELECT * FROM category WHERE id = :id")
    fun select(id: Long): CategoryEntity

    @Query("SELECT * FROM category WHERE name = :name")
    fun select(name: String): CategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: CategoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(entity: CategoryEntity)

    @Delete
    fun delete(entity: CategoryEntity)
}