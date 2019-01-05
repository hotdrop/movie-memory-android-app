package jp.hotdrop.moviememory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.hotdrop.moviememory.data.local.CategoryDatabase
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.model.Movie
import org.threeten.bp.*

@Entity(tableName = "movie")
data class MovieEntity(
        @PrimaryKey val id: Long,
        val title: String,
        val categoryId: Long,
        val overview: String?,
        val imageUrl: String?,
        val playingDate: Long?,
        val filmDirector: String?,
        val casts: List<String>?,
        val officialUrl: String?,
        val trailerMovieUrl: String?,
        val createdAt: Instant
)

fun MovieEntity.toMovie(movieNote: MovieNoteEntity?, categoryDb: CategoryDatabase): Movie {
    val category = categoryDb.find(this.categoryId).toCategory()
    val playingDate = this.playingDate?.let { LocalDate.ofEpochDay(it) }
    val watchDate = movieNote?.watchDate?.let { LocalDate.ofEpochDay(it) }
    return Movie(
            this.id,
            this.title,
            category,
            this.overview,
            this.imageUrl,
            playingDate,
            this.filmDirector,
            this.casts,
            this.officialUrl,
            this.trailerMovieUrl,
            this.createdAt,
            movieNote?.favoriteCount ?: 0,
            watchDate,
            movieNote?.watchPlace,
            movieNote?.note
    )
}

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
            id = this.id,
            title = this.title,
            categoryId = this.category.id,
            overview = this.overview,
            imageUrl = this.imageUrl,
            playingDate = this.playingDate?.toEpochDay(),
            filmDirector = this.filmDirector,
            casts = this.casts,
            officialUrl = this.officialUrl,
            trailerMovieUrl = this.trailerMovieUrl,
            createdAt = this.createAt
    )
}

fun Movie.toLocal(): MovieNoteEntity =
        MovieNoteEntity(
                this.id,
                this.favoriteCount,
                this.watchDate?.toEpochDay(),
                this.watchPlace,
                this.note
        )