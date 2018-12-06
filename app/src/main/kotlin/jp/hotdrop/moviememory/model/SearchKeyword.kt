package jp.hotdrop.moviememory.model


data class SearchKeyword(
        val id: Int? = null,
        val value: String
) {

    companion object {
        const val QUERY = "title LIKE '%' || :keyword || '%' OR overview  LIKE '%' || :keyword || '%'"
        const val LOCAL_INFO_QUERY = "note LIKE '%' || :keyword || '%'"
    }
}