package jp.hotdrop.moviememory.presentation.movie.comingsoon

import android.support.v4.app.Fragment
import jp.hotdrop.moviememory.presentation.movie.nowplaying.NowPlayingMoviesFragment

class ComingSoonMoviesFragment: Fragment() {
    companion object {
        fun newInstance() = NowPlayingMoviesFragment()
        // これはstrings.xmlから持ってくる。companionだと持ってこれないが・・
        fun title() = "近日公開"
    }
}