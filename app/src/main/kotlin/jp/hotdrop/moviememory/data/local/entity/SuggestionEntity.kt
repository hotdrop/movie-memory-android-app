package jp.hotdrop.moviememory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.hotdrop.moviememory.model.SearchKeyword

@Entity(tableName = "suggestion")
data class SuggestionEntity (
        @PrimaryKey(autoGenerate = true) var id: Int? = null,
        val keyword: String
)

fun SuggestionEntity.toSearchKeyword(): SearchKeyword =
        SearchKeyword (
                id = this.id,
                value = this.keyword
        )

fun SearchKeyword.toEntity(): SuggestionEntity =
        SuggestionEntity (
                id = this.id,
                keyword = this.value
        )