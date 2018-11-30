package jp.hotdrop.moviememory.model

import java.io.Serializable

sealed class MovieType: Serializable {

    object NowPlaying : MovieType()
    object ComingSoon : MovieType()
    object Past : MovieType()

    companion object {
        const val ARGUMENT_TAG = "ARGUMENT_TAG"
    }
}