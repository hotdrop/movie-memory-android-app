package jp.hotdrop.moviememory.presentation.movie

import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.presentation.movie.comingsoon.ComingSoonMoviesFragment
import jp.hotdrop.moviememory.presentation.movie.nowplaying.NowPlayingMoviesFragment

/**
 * トップ画面にタブを追加する時はFragmentを用意し、ここにクラスを追加する。
 */
//sealed class MovieTab {
//
//    abstract val fragment: Fragment
//    abstract val tabName: String
//
//    class NowPlayingTab : MovieTab() {
//        override val fragment = NowPlayingMoviesFragment.newInstance()
//        override val tabName = NowPlayingMoviesFragment.title()
//    }
//    class ComingSoonTab: MovieTab() {
//        override val fragment = ComingSoonMoviesFragment.newInstance()
//        override val tabName = ComingSoonMoviesFragment.title()
//    }
//}
enum class MovieTab(
        val fragment: Fragment,
        @StringRes val titleRes: Int
) {
    NOWPLAYING(NowPlayingMoviesFragment.newInstance(), R.string.tab_name_now_playing),
    COMINGSOON(ComingSoonMoviesFragment.newInstance(), R.string.tab_name_coming_soon)
}