package jp.hotdrop.moviememory.data.remote.response

import com.google.firebase.firestore.QueryDocumentSnapshot
import jp.hotdrop.moviememory.data.local.CategoryDatabase
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import jp.hotdrop.moviememory.model.Category
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime

data class MovieResponse(
        val id: Long,
        val title: String,
        val categoryName: String,
        val overview: String?,
        val imageUrl: String?,
        val playingDate: String?,
        val filmDirector: String?,
        val casts: List<String>?,
        val officialUrl: String?,
        val trailerMovieUrl: String?
)

fun QueryDocumentSnapshot.toResponse(): MovieResponse {
    fun getFieldToString(key: String): String = this.getString(key) ?: ""
    val originalPlayDate = this.getString("playingDate")
    val playingDate = if (originalPlayDate != null && originalPlayDate.length == 8) {
        originalPlayDate.substring(0, 4) + "-" + originalPlayDate.substring(4, 6) + "-" + originalPlayDate.substring(6, 8)
    } else {
        null
    }
    val casts = this.get("casts") as? List<String>
    return MovieResponse(
            this.id.toLong(),
            getFieldToString("title"),
            getFieldToString("category"),
            getFieldToString("overview"),
            getFieldToString("imageUrl"),
            playingDate,
            getFieldToString("director"),
            casts,
            getFieldToString("officialUrl"),
            getFieldToString("pvUrl")
    )
}

fun MovieResponse.toEntity(categoryMap: Map<String, Long>): MovieEntity {

    val playingDateEpoch = this.playingDate?.let {
        LocalDate.parse(it).toEpochDay()
    }

    val categoryName = if (this.categoryName.isEmpty()) {
        Category.UNSPECIFIED_NAME
    } else {
        this.categoryName
    }

    val createAtInstant = OffsetDateTime.now().toInstant()

    return MovieEntity(
            this.id,
            this.title,
            categoryMap[categoryName] ?: Category.UNSPECIFIED_ID,
            this.overview,
            this.imageUrl,
            playingDateEpoch,
            this.filmDirector,
            casts,
            this.officialUrl,
            this.trailerMovieUrl,
            createAtInstant
    )
}