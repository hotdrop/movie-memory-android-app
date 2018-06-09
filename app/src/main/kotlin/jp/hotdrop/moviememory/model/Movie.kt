package jp.hotdrop.moviememory.model

import jp.hotdrop.moviememory.presentation.parts.RecyclerDiffable
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

data class Movie(
        val id: Int,
        val title: String,
        val overview: String?,
        val imageUrl: String?,
        val playingDate: LocalDate?,
        val filmDirector: String?,
        val url: String?,
        val movieUrl: String?,
        val createAt: LocalDateTime?,
        val isSaw: Boolean,
        val sawDate: LocalDate?,
        val sawPlace: String?,
        val memo: String?
): RecyclerDiffable {
    override fun isItemTheSame(o: RecyclerDiffable) = (id == (o as? Movie)?.id)
    override fun isContentsTheSame(o: RecyclerDiffable) = (this == (o as? Movie ?: false))

    fun toTextByPlayingDate() = playingDate.toString()

    fun toTextBySawDate() = sawDate?.toString() ?: ""
}