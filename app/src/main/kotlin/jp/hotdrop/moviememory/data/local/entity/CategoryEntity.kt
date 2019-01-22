package jp.hotdrop.moviememory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.hotdrop.moviememory.model.Category

@Entity(tableName = "category")
data class CategoryEntity (
        @PrimaryKey(autoGenerate = true) val id: Long? = null,
        val name: String
)

fun CategoryEntity.toCategory(registerCount: Long = 0): Category =
        Category(
                id = this.id,
                name = this.name,
                registerCount = registerCount
        )

fun Category.toEntity(): CategoryEntity =
        CategoryEntity(
                id = this.id,
                name = this.name
        )
