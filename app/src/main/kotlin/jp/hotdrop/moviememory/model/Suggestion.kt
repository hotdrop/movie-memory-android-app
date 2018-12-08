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
}