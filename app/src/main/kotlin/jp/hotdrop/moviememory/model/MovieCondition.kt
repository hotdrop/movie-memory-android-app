package jp.hotdrop.moviememory.model

import java.io.Serializable

sealed class MovieCondition: Serializable {

    object NowPlaying : MovieCondition()
    object ComingSoon : MovieCondition()
    object Past : MovieCondition()

    companion object {
        const val ARGUMENT_TAG = "ARGUMENT_TAG"
    }
}