package jp.hotdrop.moviememory.data

import android.util.Log
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
        val convertDate = LocalDate.parse(formatterDate).toEpochDay()
        assertTrue(true)
    }
}