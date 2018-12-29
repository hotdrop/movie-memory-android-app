package jp.hotdrop.moviememory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.hotdrop.moviememory.model.Category

@Entity(tableName = "category")
data class CategoryEntity (
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val name: String
)

fun CategoryEntity.toCategory(): Category =
        Category(
                id = this.id,
                name = this.name
        )