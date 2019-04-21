package jp.hotdrop.moviememory.model

import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId

class AppDateTime constructor(
        epoch: Long = 0
) {
    private val dateTime: OffsetDateTime = when {
        epoch > 0 -> {
            val instant = Instant.ofEpochSecond(epoch)
            OffsetDateTime.ofInstant(instant, ZoneId.systemDefault())
        }
        else -> OffsetDateTime.now()
    }

    fun toEpochMilli(): Long = dateTime.toEpochSecond()

    companion object {
        fun nowEpoch(): Long {
            return OffsetDateTime.now().toInstant().epochSecond
        }
    }
}