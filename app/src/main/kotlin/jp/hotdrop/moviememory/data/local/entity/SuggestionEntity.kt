package jp.hotdrop.moviememory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.hotdrop.moviememory.model.Suggestion
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "suggestion")
data class SuggestionEntity (
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        val keyword: String,
        val createdAt: Instant
)

fun SuggestionEntity.toSuggestion(): Suggestion =
        Suggestion (
                id = this.id,
                keyword = this.keyword
        )

fun Suggestion.toEntity(): SuggestionEntity =
        SuggestionEntity (
                id = this.id,
                keyword = this.keyword,
                createdAt = OffsetDateTime.now().toInstant()
        )