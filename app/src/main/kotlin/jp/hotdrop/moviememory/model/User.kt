package jp.hotdrop.moviememory.model

data class User(
        val id: String = DEFAULT_ID,
        val name: String? = null,
        val emailAddress: String? = null,
        val isAnonymous: Boolean = true
) {
    companion object {
        private val DEFAULT_ID = "default"
    }
}