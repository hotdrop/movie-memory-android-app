package jp.hotdrop.moviememory.model

import java.io.Serializable

data class Category (
        val id: Long? = null,
        val name: String,
        val registerCount: Long = 0
): Serializable {

    fun isUnspecified(): Boolean {
        return id == UNSPECIFIED_ID
    }

    companion object {
        const val UNSPECIFIED_ID = -1L
        const val UNSPECIFIED_NAME = "未指定"
    }
}