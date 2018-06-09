package jp.hotdrop.moviememory.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import jp.hotdrop.moviememory.model.Movie
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

@Entity(tableName = "movie")
data class MovieEntity(
        @PrimaryKey var id: Int,
        var title: String,
        var overview: String?,
        var imageUrl: String?,
        var playingDate: Long?,
        var filmDirector: String?,
        var url: String?,
        var movieUrl: String?,
        var createdAt: Instant
)

fun MovieEntity.toMovie(localMovieInfo: LocalMovieInfoEntity?): Movie {
    val playingDate = this.playingDate?.let { LocalDate.ofEpochDay(it) }
    val createAt = LocalDateTime.ofInstant(this.createdAt, ZoneId.systemDefault())
    val sawDate = localMovieInfo?.sawDate?.let { LocalDate.ofEpochDay(it) }
    return Movie(
            this.id,
            this.title,
            this.overview,
            this.imageUrl,
            playingDate,
            this.filmDirector,
            this.url,
            this.movieUrl,
            createAt,
            localMovieInfo?.isSaw ?: false,
            sawDate,
            localMovieInfo?.sawPlace,
            localMovieInfo?.memo
    )
}