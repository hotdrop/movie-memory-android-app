package jp.hotdrop.moviememory.model

import jp.hotdrop.moviememory.presentation.parts.RecyclerDiffable
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

data class Movie(
        val id: Int,
        val title: String,
        val category: Category,
        val overview: String?,
        val imageUrl: String?,
        val playingDate: LocalDate?,
        val filmDirector: String?,
        val officialUrl: String?,
        val trailerMovieUrl: String?,
        val createAt: LocalDateTime?,
        var favoriteCount: Int,
        var watchDate: LocalDate?,
        var watchPlace: String?,
        var note: String?
): RecyclerDiffable {

    fun toTextPlayingDate() = playingDate.toString()
    fun toTextWatchDate() = watchDate?.toString() ?: ""
    fun toTextFavoriteCount() = favoriteCount.toString()

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

    override fun equals(other: Any?): Boolean {
        return (other as Movie).id == id || super.equals(other)
    }

    override fun hashCode(): Int {
        return id
    }

    // 以下2つはRecyclerViewのDiff用
    override fun isItemTheSame(o: RecyclerDiffable) =
            (id == (o as? Movie)?.id)

    override fun isContentsTheSame(o: RecyclerDiffable) =
            (this == (o as? Movie ?: false))
}