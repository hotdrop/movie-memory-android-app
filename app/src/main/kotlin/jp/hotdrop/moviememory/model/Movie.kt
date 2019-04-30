package jp.hotdrop.moviememory.model

data class Movie(
        val id: Long,
        var title: String,
        val category: Category,
        var overview: String?,
        var imageUrl: String?,
        val playingDate: AppDate?,
        var filmDirector: String?,
        val originalAuthor: String?,
        val casts: List<Cast>?,
        var officialUrl: String?,
        var trailerMovieUrl: String?,
        val distribution: String?, // 制作元のこと
        val makeCountry: String?,
        val makeYear: Int?,
        val playTime: Int?,
        val createdAt: Long,
        var favoriteCount: Int,
        var watchDate: AppDate?,
        var watchPlace: String?,
        var note: String?
) {

    fun toTextPlayTime() = playTime?.toString() ?: ""
    fun toTextPlayingDate() = playingDate?.toString() ?: DEFAULT_TEXT_VALUE
    fun toTextFavoriteCount() = favoriteCount.toString()
    fun toTextFilmDirector() = filmDirector ?: DEFAULT_TEXT_VALUE
    fun toTextOriginalAuthor() = originalAuthor ?: DEFAULT_TEXT_VALUE
    fun toTextDistribution() = distribution ?: DEFAULT_TEXT_VALUE
    fun toTextMakeCountry() = makeCountry ?: DEFAULT_TEXT_VALUE
    fun toTextMakeYear() = makeYear?.toString() ?: DEFAULT_TEXT_VALUE

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

    companion object {
        const val DEFAULT_TEXT_VALUE = "ー"
    }
}

// キャラは必ず存在するが担当する俳優はこの時点で未定の可能性があるためnull許容としている
data class Cast(val name: String, val actor: String?)