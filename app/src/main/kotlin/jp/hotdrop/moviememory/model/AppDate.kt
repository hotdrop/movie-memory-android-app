package jp.hotdrop.moviememory.model

import org.threeten.bp.LocalDate

class AppDate constructor(
        epoch: Long = 0,
        dateStr: String? = null,
        localDate: LocalDate? = null
) {

    private val date: LocalDate = when {
        epoch > 0 -> LocalDate.ofEpochDay(epoch)
        dateStr != null -> toLocalDate(dateStr)
        localDate != null -> localDate
        else -> LocalDate.now()
    }

    val year: Int
        get() = date.year

    val month: Int
        get() = date.monthValue

    val day: Int
        get() = date.dayOfMonth

    fun toEpochDay(): Long = date.toEpochDay()

    fun minusMonths(monthsToSubtract: Long): AppDate {
        return AppDate(localDate = date.minusMonths(monthsToSubtract))
    }

    fun plusDays(daysToSubtract: Long): AppDate {
        return AppDate(localDate = date.plusDays(daysToSubtract))
    }

    fun minusDays(daysToSubtract: Long): AppDate {
        return AppDate(localDate = date.minusDays(daysToSubtract))
    }

    override fun toString(): String {
        return date.toString()
    }

    private fun toLocalDate(str: String): LocalDate {
        val dateSplit = str.split("-")
        val dateStr = String.format("%s-%s-%s",
                dateSplit[0],
                dateSplit[1].padStart(2, '0'),
                dateSplit[2].padStart(2, '0'))
        return LocalDate.parse(dateStr)
    }

    companion object {
        /**
         * 2019-1-2のような文字からLocalDateのEpochを取得して返す。
         * この形式でない文字がきたらnullを返す
         */
        fun toEpochFormatHyphen(str: String?): Long? {
            str ?: return null
            val dateSplit = str.split("-")
            if (dateSplit.size != 3) {
                return null
            }

            val dateStr = String.format("%s-%s-%s",
                    dateSplit[0],
                    dateSplit[1].padStart(2, '0'),
                    dateSplit[2].padStart(2, '0'))
            return LocalDate.parse(dateStr).toEpochDay()
        }

        fun nowEpochDay(): Long = LocalDate.now().toEpochDay()

        fun of(year: Int, month: Int, day: Int): AppDate {
            return AppDate(localDate = LocalDate.of(year, month, day))
        }
    }
}