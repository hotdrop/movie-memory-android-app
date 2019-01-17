package jp.hotdrop.moviememory.model

import java.io.Serializable

data class Category (
        val id: Long,
        val name: String,
        val registerCount: Long
): Serializable {
    companion object {
        const val UNSPECIFIED_ID = -1L
        const val UNSPECIFIED_NAME = "未指定"
    }
}