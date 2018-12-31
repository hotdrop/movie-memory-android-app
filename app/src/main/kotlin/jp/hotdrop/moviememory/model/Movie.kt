package jp.hotdrop.moviememory.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

data class Movie(
        val id: Long,
        var title: String,
        var category: Category,
        var overview: String?,
        var imageUrl: String?,
        val playingDate: LocalDate?,
        var filmDirector: String?,
        val casts: List<String>?,
        var officialUrl: String?,
        var trailerMovieUrl: String?,
        val createAt: LocalDateTime?,
        var favoriteCount: Int,
        var watchDate: LocalDate?,
        var watchPlace: String?,
        var note: String?
) {

    fun toTextPlayingDate() = playingDate?.toString() ?: "ー"
    fun toTextWatchDate() = watchDate?.toString() ?: "ー"
    fun toTextFavoriteCount() = favoriteCount.toString()
    fun toTextFilmDirector() = filmDirector ?: "ー"
    fun toTextWatchPlace() = watchPlace ?: "ー"

    fun categoryName() = category.name

    fun setWatchDateFromText(strSawDate: String) {
        watchDate = LocalDate.parse(strSawDate)
    }

    fun update(newInfo: Movie) {
        favoriteCount = newInfo.favoriteCount
        watchDate = newInfo.watchDate
        watchPlace = newInfo.watchPlace
        note = newInfo.note
    }

    override fun equals(other: Any?): Boolean = (other as Movie).id == id || super.equals(other)

    override fun hashCode(): Int = id.toInt()
}