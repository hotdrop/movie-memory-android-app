package jp.hotdrop.moviememory.presentation.component

import com.airbnb.lottie.LottieAnimationView

class FavoriteStars constructor(
        private val lottieViews: List<LottieAnimationView>,
        private var favoriteCount: Int = 0
) {

    init {
        lottieViews.forEachIndexed { index, lottieAnimationView ->
            lottieAnimationView.setOnClickListener {
                // つけてる星の数と一致した場合は全部リセット。それ以外は基本play
                if (lottieAnimationView.isActivated && index + 1 == favoriteCount) {
                    clear()
                    favoriteCount = 0
                } else {
                    favoriteCount = index + 1
                    playAnimations(favoriteCount)
                }
            }
        }
        playAnimations(favoriteCount)
    }

    fun count(): Int = favoriteCount

    private fun playAnimations(toNum: Int) {
        clear()
        favoriteCount = toNum
        lottieViews.take(toNum)
                .forEach { it.play() }
    }

    private fun clear() {
        favoriteCount = 0
        lottieViews.forEach { it.reset() }
    }

    private fun LottieAnimationView.play(): LottieAnimationView = this.apply {
        playAnimation()
        isActivated = true
    }

    private fun LottieAnimationView.reset(): LottieAnimationView = this.apply {
        cancelAnimation()
        progress = 0f
        isActivated = false
    }
}