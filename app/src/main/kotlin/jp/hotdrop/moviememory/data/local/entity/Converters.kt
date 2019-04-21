package jp.hotdrop.moviememory.data.local.entity

import androidx.room.TypeConverter

object Converters {

    private const val ARRAY_SEPARATOR = "@@"

    @TypeConverter
    @JvmStatic fun fromArrayString(value: String?): List<String>? {
        return if (value.isNullOrEmpty()) {
            null
        } else {
            value.split(ARRAY_SEPARATOR)
        }
    }

    @TypeConverter
    @JvmStatic fun arrayStringToLongString(arr: List<String>?): String? =
            arr?.joinToString(separator = ARRAY_SEPARATOR)
}