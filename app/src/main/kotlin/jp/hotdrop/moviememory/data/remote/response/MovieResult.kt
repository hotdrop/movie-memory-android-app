package jp.hotdrop.moviememory.data.remote.response

import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime

data class MovieResult(
        val id: Int,
        val title: String,
        val categoryId: Int,
        val categoryName: String,
        val overview: String?,
        val imageUrl: String?,
        val playingDate: String?,
        val filmDirector: String?,
        val officialUrl: String?,
        val trailerMovieUrl: String?
)

fun MovieResult.toMovieEntity(): MovieEntity {
    val playingDateEpoch = this.playingDate?.let { LocalDate.parse(it).toEpochDay() }
    val createAtInstant = OffsetDateTime.now().toInstant()
    return MovieEntity(
            this.id,
            this.title,
            this.categoryId,
            this.categoryName,
            this.overview,
            this.imageUrl,
            playingDateEpoch,
            this.filmDirector,
            this.officialUrl,
            this.trailerMovieUrl,
            createAtInstant
    )
}