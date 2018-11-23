package jp.hotdrop.moviememory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.model.Movie
import org.threeten.bp.*

@Entity(tableName = "movie")
data class MovieEntity(
        @PrimaryKey var id: Int,
        var title: String,
        var categoryId: Int,
        var categoryName: String,
        var overview: String?,
        var imageUrl: String?,
        var playingDate: Long?,
        var filmDirector: String?,
        var officialUrl: String?,
        var trailerMovieUrl: String?,
        var createdAt: Instant
)

fun MovieEntity.toMovie(localMovieInfo: LocalMovieInfoEntity?): Movie {
    val playingDate = this.playingDate?.let { LocalDate.ofEpochDay(it) }
    val createAt = LocalDateTime.ofInstant(this.createdAt, ZoneId.systemDefault())
    val watchDate = localMovieInfo?.watchDate?.let { LocalDate.ofEpochDay(it) }
    return Movie(
            this.id,
            this.title,
            Category(this.categoryId, this.categoryName),
            this.overview,
            this.imageUrl,
            playingDate,
            this.filmDirector,
            this.officialUrl,
            this.trailerMovieUrl,
            createAt,
            watchDate,
            localMovieInfo?.watchPlace,
            localMovieInfo?.note
    )
}

fun Movie.toLocal(): LocalMovieInfoEntity =
        LocalMovieInfoEntity(
                this.id,
                this.watchDate?.toEpochDay(),
                this.watchPlace,
                this.note
        )