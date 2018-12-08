package jp.hotdrop.moviememory.model

import jp.hotdrop.moviememory.presentation.parts.RecyclerDiffable


data class Suggestion(
        val id: Int? = null,
        val keyword: String
): RecyclerDiffable {

    override fun isItemTheSame(o: RecyclerDiffable) =
            (keyword == (o as? Suggestion)?.keyword)

    override fun isContentsTheSame(o: RecyclerDiffable) =
            (this == (o as? Suggestion ?: false))

    companion object {
        const val QUERY = "title LIKE '%' || :keyword || '%' OR overview  LIKE '%' || :keyword || '%'"
        const val LOCAL_INFO_QUERY = "note LIKE '%' || :keyword || '%'"
    }
}