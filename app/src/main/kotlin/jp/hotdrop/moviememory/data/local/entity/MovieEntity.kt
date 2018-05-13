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
        var playingDate: Instant?,
        var filmDirector: String?,
        var url: String?,
        var movieUrl: String?,
        var createdAt: Instant
)

fun MovieEntity.toNowPlayingMovie(localMovieInfo: LocalMovieInfoEntity?): Movie {
    val playingDate = this.playingDate?.let { LocalDate.ofEpochDay(it.epochSecond) }
    val createAt = LocalDateTime.ofInstant(this.createdAt, ZoneId.systemDefault())
    val sawDate = localMovieInfo?.sawDate?.let { LocalDate.ofEpochDay(it.epochSecond) }
    return Movie(
            this.id,
            this.title,
            this.overview,
            this.imageUrl,
            playingDate,
            "",
            "",
            "",
            createAt,
            localMovieInfo?.isSaw ?: false,
            sawDate,
            localMovieInfo?.memo,
            Movie.Status.NowPlaying
    )
}