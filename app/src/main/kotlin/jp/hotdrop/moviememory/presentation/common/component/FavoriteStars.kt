package jp.hotdrop.moviememory.presentation.common.component

import com.airbnb.lottie.LottieAnimationView

class FavoriteStars constructor(
        private val lottieViews: List<LottieAnimationView>,
        private var favoriteCount: Int = 0,
        private val changeStateListener: (Int) -> Unit
) {

    init {
        lottieViews.forEachIndexed { index, lottieAnimationView ->
            lottieAnimationView.setOnClickListener {
                // つけてる星の数と一致した場合は全部リセット。それ以外は基本play
                if (lottieAnimationView.isActivated && index + 1 == favoriteCount) {
                    favoriteCount = 0
                    changeStateListener(0)
                    clear()
                } else {
                    favoriteCount = index + 1
                    changeStateListener(favoriteCount)
                    playAnimations(favoriteCount)
                }
            }
        }
        playAnimations(favoriteCount)
    }

    private fun playAnimations(toNum: Int) {
        clear()
        lottieViews.take(toNum)
                .forEach { it.play() }
    }

    private fun clear() {
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