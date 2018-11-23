package jp.hotdrop.moviememory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_local_info")
class LocalMovieInfoEntity (
        // TODO ローカルで保存できるものもっと増やす。URLやカテゴリー
        @PrimaryKey var id: Int,
        var watchDate: Long?,
        var watchPlace: String?,
        var note: String?
)