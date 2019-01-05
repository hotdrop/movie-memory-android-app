package jp.hotdrop.moviememory.model

import org.threeten.bp.Instant
import org.threeten.bp.LocalDate

data class Movie(
        val id: Long,
        var title: String,
        val category: Category,
        var overview: String?,
        var imageUrl: String?,
        val playingDate: LocalDate?,
        var filmDirector: String?,
        val casts: List<String>?,
        var officialUrl: String?,
        var trailerMovieUrl: String?,
        val createAt: Instant,
        var favoriteCount: Int,
        val watchDate: LocalDate?,
        var watchPlace: String?,
        var note: String?
) {

    fun toTextPlayingDate() = playingDate?.toString() ?: DEFAULT_TEXT_VALUE
    fun toTextWatchDate() = watchDate?.toString() ?: DEFAULT_TEXT_VALUE
    fun toTextFavoriteCount() = favoriteCount.toString()
    fun toTextFilmDirector() = filmDirector ?: DEFAULT_TEXT_VALUE
    fun toTextWatchPlace() = watchPlace ?: DEFAULT_TEXT_VALUE

    fun categoryName() = category.name

    companion object {
        const val DEFAULT_TEXT_VALUE = "ー"
        fun copyAll(m: Movie) =
                Movie(m.id, m.title, m.category, m.overview, m.imageUrl,
                        m.playingDate, m.filmDirector, m.casts, m.officialUrl,
                        m.trailerMovieUrl, m.createAt, m.favoriteCount, m.watchDate,
                        m.watchPlace,m.note)
    }
}