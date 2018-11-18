package jp.hotdrop.moviememory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.hotdrop.moviememory.model.Movie
import org.threeten.bp.*

@Entity(tableName = "movie")
data class MovieEntity(
        @PrimaryKey var id: Int,
        var title: String,
        var overview: String?,
        var imageUrl: String?,
        var playingDate: Long?,
        var filmDirector: String?,
        var url: String?,
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
            createAt,
            localMovieInfo?.isSaw ?: false,
            sawDate,
            localMovieInfo?.sawPlace,
            localMovieInfo?.memo
    )
}