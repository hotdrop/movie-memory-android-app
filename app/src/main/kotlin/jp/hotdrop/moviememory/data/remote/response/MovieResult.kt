package jp.hotdrop.moviememory.data.remote.response

import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

data class MovieResult(
        val id: Int,
        val title: String,
        val overview: String?,
        val imageUrl: String?,
        val playingDate: String?,
        val filmDirector: String?,
        val url: String?,
        val movieUrl: String?,
        val createdAt: String
)

fun MovieResult.toMovieEntity(): MovieEntity {
    val playingDateEpoch = this.playingDate?.let { LocalDate.parse(it).toEpochDay() }
    val createAtInstant = this.createdAt.let {
        LocalDateTime.parse(it).atZone(ZoneId.systemDefault()).toInstant()
    }
    return MovieEntity(
            this.id,
            this.title,
            this.overview,
            this.imageUrl,
            playingDateEpoch,
            "",
            "",
            "",
            createAtInstant
    )
}