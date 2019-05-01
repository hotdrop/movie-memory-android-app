package jp.hotdrop.moviememory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.hotdrop.moviememory.data.local.database.CategoryDatabase
import jp.hotdrop.moviememory.model.AppDate
import jp.hotdrop.moviememory.model.Cast
import jp.hotdrop.moviememory.model.Movie

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
        val createdAt: Long,
        val originalAuthor: String?,
        val distribution: String?,
        val makeCountry: String?,
        val makeYear: Int?,
        val playTime: Int?
)

fun MovieEntity.toMovie(movieNote: MovieNoteEntity?, categoryDb: CategoryDatabase): Movie {

    val category = categoryDb.find(this.categoryId).toCategory()
    val playingDate = this.playingDate?.let { AppDate(it) }
    val watchDate = movieNote?.watchDate?.let { AppDate(it) }
    val casts = this.casts
            ?.map { it.split(Movie.CAST_SEPARATOR) }
            ?.filter { it.size > 1 }
            ?.map {
                if (it.size == 2) {
                    Cast(it[1], it[0])
                } else {
                    Cast(it[0], null)
                }
            }

    return Movie(
            this.id,
            this.title,
            category,
            this.overview,
            this.imageUrl,
            playingDate,
            this.filmDirector,
            this.originalAuthor,
            casts,
            this.officialUrl,
            this.trailerMovieUrl,
            this.distribution,
            this.makeCountry,
            this.makeYear,
            this.playTime,
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
            categoryId = this.category.id!!, // ここのカテゴリーIDは必ず存在する。しなければバグなのでnpeで落とす
            overview = this.overview,
            imageUrl = this.imageUrl,
            playingDate = this.playingDate?.toEpochDay(),
            filmDirector = this.filmDirector,
            casts = this.casts?.map { String.format("%s${Movie.CAST_SEPARATOR}%s", it.actor, it.charName) },
            officialUrl = this.officialUrl,
            trailerMovieUrl = this.trailerMovieUrl,
            createdAt = this.createdAt,
            originalAuthor = this.originalAuthor,
            distribution = this.distribution,
            makeCountry = this.makeCountry,
            makeYear = this.makeYear,
            playTime = this.playTime
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