package jp.hotdrop.moviememory.util

import org.threeten.bp.LocalDate

object DateParser {

    /**
     * 2019-1-2のような文字からLocalDateのEpochを取得して返す。
     * この形式でない文字がきたらnullを返す
     */
    fun toEpochFormatHyphen(str: String): Long? {
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
}