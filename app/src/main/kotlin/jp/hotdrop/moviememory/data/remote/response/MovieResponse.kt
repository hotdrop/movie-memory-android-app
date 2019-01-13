package jp.hotdrop.moviememory.data.remote.response

import com.google.firebase.firestore.QueryDocumentSnapshot
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import jp.hotdrop.moviememory.model.Category
import org.threeten.bp.LocalDate

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
        val trailerMovieUrl: String?,
        val createdAt: Long
)

fun QueryDocumentSnapshot.toResponse(): MovieResponse {

    fun getFieldToString(key: String): String = this.getString(key) ?: ""
    fun getFieldToLong(key: String): Long = this.getLong(key) ?: 0

    val casts = this.get("casts") as? List<String>
    return MovieResponse(
            this.id.toLong(),
            getFieldToString("title"),
            getFieldToString("category"),
            getFieldToString("overview"),
            getFieldToString("imageUrl"),
            getFieldToString("playingDate"),
            getFieldToString("director"),
            casts,
            getFieldToString("officialUrl"),
            getFieldToString("pvUrl"),
            getFieldToLong("createdAt")
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
            this.createdAt
    )
}