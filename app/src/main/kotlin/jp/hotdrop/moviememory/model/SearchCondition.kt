package jp.hotdrop.moviememory.model

import java.io.Serializable

sealed class SearchCondition: Serializable {

    class Keyword (
            val keyword: String = NONE
    ): SearchCondition() {
        // メモリで持っているオブジェクト用。必ずcompanion objectに定義しているRoomのLike条件と一致させること
        fun condition(movie: Movie): Boolean {
            return (movie.title.contains(keyword) ||
                    movie.overview?.contains(keyword) == true ||
                    movie.note?.contains(keyword) == true)
        }
        companion object {
            const val NONE = ""
        }
    }

    class Category (
            val category: jp.hotdrop.moviememory.model.Category
    ): SearchCondition()

    class Favorite (
            val moreThanNum: Int
    ): SearchCondition()

    companion object {
        // Roomで使用する用
        const val LIKE_KEYWORD = "title LIKE '%' || :keyword || '%' OR overview  LIKE '%' || :keyword || '%'"
        const val LIKE_KEYWORD_BY_LOCAL = "note LIKE '%' || :keyword || '%'"
    }
}