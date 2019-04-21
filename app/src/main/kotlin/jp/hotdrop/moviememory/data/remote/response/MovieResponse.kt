package jp.hotdrop.moviememory.data.remote.response

import com.google.firebase.firestore.QueryDocumentSnapshot
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import jp.hotdrop.moviememory.model.AppDate
import jp.hotdrop.moviememory.model.Category

data class MovieResponse(
        val id: Long,
        val title: String,
        val categoryName: String,
        val overview: String?,
        val imageUrl: String?,
        val playingDate: String?,
        val originalAuthor: String?,
        val filmDirector: String?,
        val casts: List<String>?,
        val distribution: String?,
        val makeCountry: String?,
        val makeYear: String?,
        val playTime: String?,
        val officialUrl: String?,
        val trailerMovieUrl: String?,
        val createdAt: Long
)

fun QueryDocumentSnapshot.toResponse(): MovieResponse {

    fun getFieldToString(key: String): String = this.getString(key) ?: ""
    fun getFieldToLong(key: String): Long = this.getLong(key) ?: 0

    val casts = (this.get("casts") as? List<*>)?.filterIsInstance<String>()
    return MovieResponse(
            this.id.toLong(),
            getFieldToString("title"),
            getFieldToString("category"),
            getFieldToString("overview"),
            getFieldToString("imageUrl"),
            getFieldToString("playingDate"),
            getFieldToString("originalAuthor"),
            getFieldToString("director"),
            casts,
            getFieldToString("distribution"),
            getFieldToString("makeCountry"),
            getFieldToString("makeYear"),
            getFieldToString("playTime"),
            getFieldToString("officialUrl"),
            getFieldToString("pvUrl"),
            getFieldToLong("createdAt")
    )
}

fun MovieResponse.toEntity(categoryMap: Map<String, Long>): MovieEntity {

    val playingDateEpoch = AppDate.toEpochFormatHyphen(this.playingDate)
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
            this.originalAuthor,
            casts,
            this.officialUrl,
            this.trailerMovieUrl,
            this.createdAt,
            this.filmDirector,
            this.distribution,
            this.makeCountry,
            this.makeYear?.replace("年", "")?.toIntOrNull(),
            this.playTime?.replace("分", "")?.toIntOrNull()
    )
}