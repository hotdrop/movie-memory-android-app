package jp.hotdrop.moviememory.model


inline class SearchKeyword(val value: String) {

    companion object {
        const val QUERY = "title LIKE '%' || :keyword || '%' OR overview  LIKE '%' || :keyword || '%'"
        const val LOCAL_INFO_QUERY = "note LIKE '%' || :keyword || '%'"
    }
}