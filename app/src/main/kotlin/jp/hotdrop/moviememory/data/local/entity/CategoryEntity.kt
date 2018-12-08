package jp.hotdrop.moviememory.data.local.entity

import androidx.room.ColumnInfo
import jp.hotdrop.moviememory.model.Category

data class CategoryEntity (
        @ColumnInfo(name = "categoryId")
        var id: Int,

        @ColumnInfo(name = "categoryName")
        var name: String
)

fun CategoryEntity.toCategory(): Category =
        Category(
                id = this.id,
                name = this.name
        )