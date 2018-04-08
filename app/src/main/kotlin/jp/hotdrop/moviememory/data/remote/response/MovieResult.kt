package jp.hotdrop.moviememory.data.remote.response

data class MovieResult(
        // TODO もっと色々あるが一旦これで
        val id: Int,
        val title: String,
        val overview: String,
        val posterPath: String?,
        val releaseDate: String
)