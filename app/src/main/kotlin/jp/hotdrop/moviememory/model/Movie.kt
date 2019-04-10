package jp.hotdrop.moviememory.model

import org.threeten.bp.LocalDate

data class Movie(
        val id: Long,
        var title: String,
        val category: Category,
        var overview: String?,
        var imageUrl: String?,
        val playingDate: LocalDate?,
        var filmDirector: String?,
        val originalAuthor: String?,
        val casts: List<Cast>?,
        var officialUrl: String?,
        var trailerMovieUrl: String?,
        val distribution: String?,
        val makeCountry: String?,
        val makeYear: Int?,
        val playTime: Int?,
        val createdAt: Long,
        var favoriteCount: Int,
        var watchDate: LocalDate?,
        var watchPlace: String?,
        var note: String?
) {

    fun toTextPlayingDate() = playingDate?.toString() ?: DEFAULT_TEXT_VALUE
    fun toTextWatchDate() = watchDate?.toString() ?: DEFAULT_TEXT_VALUE
    fun toTextFavoriteCount() = favoriteCount.toString()
    fun toTextFilmDirector() = filmDirector ?: DEFAULT_TEXT_VALUE
    fun toTextWatchPlace() = watchPlace ?: DEFAULT_TEXT_VALUE

    fun categoryName() = category.name

    override fun equals(other: Any?): Boolean {
        return (other as? Movie)?.id == this.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        const val DEFAULT_TEXT_VALUE = "ー"
    }
}

// キャラは必ず存在するが担当する俳優はこの時点で未定の可能性があるためnull許容としている
data class Cast(val name: String, val actor: String?)