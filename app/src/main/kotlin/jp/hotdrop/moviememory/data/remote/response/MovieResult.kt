package jp.hotdrop.moviememory.data.remote.response

data class MovieResult(
        val id: Int,
        val title: String,
        val overview: String?,
        val imageUrl: String?,
        val playingDate: String?,
        val filmDirector: String?,
        val url: String?,
        val movieUrl: String?,
        val createdAt: String
)