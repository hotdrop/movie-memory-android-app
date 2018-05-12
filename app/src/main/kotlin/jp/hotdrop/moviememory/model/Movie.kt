package jp.hotdrop.moviememory.model

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
        val createAt: LocalDateTime,
        val isSaw: Boolean,
        val sawDate: LocalDate?,
        val memo: String?,
        val status: Status
) {
    enum class Status {
        NowPlaying,
        ComingSoon,
        Played
    }
}