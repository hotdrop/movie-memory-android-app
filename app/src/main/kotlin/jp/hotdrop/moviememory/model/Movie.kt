package jp.hotdrop.moviememory.model

data class Movie(
        val id: Long,
        var title: String,
        val category: Category,
        var overview: String?,
        var imageUrl: String?,
        val playingDate: AppDate?,
        var filmDirector: String?,
        var originalAuthor: String?,
        val casts: List<Cast>?,
        var officialUrl: String?,
        var trailerMovieUrl: String?,
        var distribution: String?, // 制作元のこと
        var makeCountry: String?,
        val makeYear: Int?,
        val playTime: Int?,
        val createdAt: Long,
        var favoriteCount: Int,
        var watchDate: AppDate?,
        var watchPlace: String?,
        var note: String?
) {

    fun toTextPlayTime() = playTime?.let { String.format("${playTime}分") } ?: ""
    fun toTextPlayingDate() = playingDate?.toString() ?: DEFAULT_TEXT_VALUE
    fun toTextFavoriteCount() = favoriteCount.toString()
    fun toTextFilmDirector() = filmDirector ?: DEFAULT_TEXT_VALUE
    fun toTextOriginalAuthor() = originalAuthor ?: DEFAULT_TEXT_VALUE
    fun toTextDistribution() = distribution ?: DEFAULT_TEXT_VALUE
    fun toTextMakeCountry() = makeCountry ?: DEFAULT_TEXT_VALUE
    fun toTextMakeYear() = makeYear?.let { String.format("${playTime}年") } ?: DEFAULT_TEXT_VALUE

    fun toTextWatchDate() = watchDate?.toString() ?: ""
    fun toTextWatchPlace() = watchPlace ?: ""
    fun toTextNote() = note ?: ""

    fun categoryName() = category.name

    override fun equals(other: Any?): Boolean {
        return (other as? Movie)?.id == this.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    fun toMap(): Map<String, Any> {
        return hashMapOf("title" to title,
                "category" to category.name,
                "overview" to (overview ?: ""),
                "imageUrl" to (imageUrl ?: ""),
                "playingDate" to (playingDate?.toString() ?: ""),
                "director" to (filmDirector ?: ""),
                "originalAuthor" to (originalAuthor ?: ""),
                "officialUrl" to (officialUrl ?: ""),
                "pvUrl" to (trailerMovieUrl ?: ""),
                "distribution" to (distribution ?: ""),
                "makeCountry" to (makeCountry ?: ""),
                "makeYear" to (makeYear?.toString() ?: ""),
                "playTime" to (playTime?.toString() ?: ""),
                "casts" to (casts?.map { String.format("%s${CAST_SEPARATOR}%s", it.actor, it.charName) } ?: "")
        )
    }

    companion object {
        const val DEFAULT_TEXT_VALUE = "ー"
        const val CAST_SEPARATOR = " : "
    }
}

data class Cast(val actor: String, val charName: String?)