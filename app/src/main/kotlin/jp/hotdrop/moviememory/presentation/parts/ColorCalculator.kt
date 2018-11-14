package jp.hotdrop.moviememory.presentation.parts

import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils

object ColorCalculator {

    @JvmStatic
    fun calc(fraction: Float, @ColorInt color: Int): Int {
        return ColorUtils.setAlphaComponent(color, (255 * fraction).toInt())
    }
}