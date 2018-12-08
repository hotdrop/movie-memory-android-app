package jp.hotdrop.moviememory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.hotdrop.moviememory.model.Suggestion

@Entity(tableName = "suggestion")
data class SuggestionEntity (
        @PrimaryKey(autoGenerate = true) var id: Int? = null,
        val keyword: String
)

fun SuggestionEntity.toSuggestion(): Suggestion =
        Suggestion (
                id = this.id,
                keyword = this.keyword
        )

fun Suggestion.toEntity(): SuggestionEntity =
        SuggestionEntity (
                id = this.id,
                keyword = this.keyword
        )