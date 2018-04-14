package jp.hotdrop.moviememory.presentation.movie.nowplaying

import android.support.v4.app.Fragment

class NowPlayingMoviesFragment: Fragment() {

    companion object {
        fun newInstance() = NowPlayingMoviesFragment()
        // これはstrings.xmlから持ってくる。companionだと持ってこれないが・・
        fun title() = "公開中"
    }
}