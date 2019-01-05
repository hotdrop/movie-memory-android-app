package jp.hotdrop.moviememory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO ローカルで保存できるものもっと増やす。URLやカテゴリー
@Entity(tableName = "movie_note")
data class MovieNoteEntity (
        @PrimaryKey val id: Long,
        var favoriteCount: Int,
        var watchDate: Long?,
        var watchPlace: String?,
        var note: String?
)