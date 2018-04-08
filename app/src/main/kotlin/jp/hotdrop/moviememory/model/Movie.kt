package jp.hotdrop.moviememory.model

data class Movie(
        // TODO まだたくさんあるが画面決まってないのでこれだけ
        val id: Int,
        val title: String,
        val overview: String,
        val status: Status
) {
    enum class Status {
        NowPlaying,
        ComingSoon,
        Played
    }
}