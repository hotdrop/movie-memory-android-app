package jp.hotdrop.moviememory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_local_info")
class LocalMovieInfoEntity (
        @PrimaryKey var id: Int,
        var isSaw: Boolean,
        var sawDate: Long?,
        var sawPlace: String?,
        var memo: String?
)