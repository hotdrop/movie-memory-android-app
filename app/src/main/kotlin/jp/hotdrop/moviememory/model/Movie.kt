package jp.hotdrop.moviememory.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

data class Movie(
        val id: Long,
        val title: String,
        val category: Category,
        val overview: String?,
        val imageUrl: String?,
        val playingDate: LocalDate?,
        val filmDirector: String?,
        val casts: List<String>?,
        val officialUrl: String?,
        val trailerMovieUrl: String?,
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
    fun toTectWatchPlace() = watchPlace ?: "ー"

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