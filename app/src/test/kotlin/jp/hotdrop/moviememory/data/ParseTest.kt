package jp.hotdrop.moviememory.data

import jp.hotdrop.moviememory.model.AppDate
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ParseTest {

    @Test
    fun parse() {
        val originalPlayDate = "20181102"
        val formatterDate = originalPlayDate.substring(0, 4) + "-" + originalPlayDate.substring(4, 6) + "-" + originalPlayDate.substring(6, 8)
        assertEquals("2018-11-02", formatterDate)
        // ここはparseで落ちなければOK
        AppDate.toEpochFormatHyphen(formatterDate)

        val dateSplit = "2019-4-1".split("-")
        val dateStr = String.format("%s-%s-%s", dateSplit[0], dateSplit[1].padStart(2, '0'), dateSplit[2].padStart(2, '0'))
        assertEquals("2019-04-01", dateStr)
    }
}