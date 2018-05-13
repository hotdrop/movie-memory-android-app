package jp.hotdrop.moviememory.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.Instant

@Entity(tableName = "movie_local_info")
class LocalMovieInfoEntity (
        @PrimaryKey var id: Int,
        var isSaw: Boolean,
        var sawDate: Instant?,
        var memo: String?
)