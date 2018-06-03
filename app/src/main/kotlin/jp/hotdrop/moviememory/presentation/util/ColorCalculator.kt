package jp.hotdrop.moviememory.presentation.util

import android.support.annotation.ColorInt
import android.support.v4.graphics.ColorUtils

object ColorCalculator {

    @JvmStatic
    fun calc(fraction: Float, @ColorInt color: Int): Int {
        return ColorUtils.setAlphaComponent(color, (255 * fraction).toInt())
    }
}