package jp.hotdrop.moviememory.data.local.entity

import android.arch.persistence.room.TypeConverter
import org.threeten.bp.Instant

object Converters {

    @TypeConverter
    @JvmStatic fun fromTimestamp(value: Long?): Instant? =
            if (value == null) {
                null
            } else {
                Instant.ofEpochSecond(value)
            }

    @TypeConverter
    @JvmStatic fun dateToTimestamp(instant: Instant?): Long? =
            instant?.epochSecond
}