package jp.hotdrop.moviememory.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.threeten.bp.LocalDate

@RunWith(JUnit4::class)
class ParseTest {

    @Test
    fun parse() {
        val originalPlayDate = "20181102"
        val formatterDate = originalPlayDate.substring(0, 4) + "-" + originalPlayDate.substring(4, 6) + "-" + originalPlayDate.substring(6, 8)
        assertEquals("2018-11-02", formatterDate)
        // ここはparseで落ちなければOK
        LocalDate.parse(formatterDate).toEpochDay()

        val dateSplit = "2019-4-1".split("-")
        val dateStr = String.format("%s-%s-%s", dateSplit[0], dateSplit[1].padStart(2, '0'), dateSplit[2].padStart(2, '0'))
        assertEquals("2019-04-01", dateStr)
    }
}