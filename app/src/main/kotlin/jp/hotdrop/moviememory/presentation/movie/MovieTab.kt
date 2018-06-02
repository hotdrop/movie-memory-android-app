package jp.hotdrop.moviememory.presentation.movie

import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.presentation.movie.comingsoon.ComingSoonMoviesFragment
import jp.hotdrop.moviememory.presentation.movie.nowplaying.NowPlayingMoviesFragment

/**
 * トップ画面にタブを追加する時はFragmentを用意し、ここにクラスを追加する。
 * このEnumやめたほうがいいのではなかろうか・・
 */
enum class MovieTab(
        val fragment: Fragment,
        @StringRes val titleRes: Int
) {
    NowPlaying(NowPlayingMoviesFragment.newInstance(), R.string.tab_name_now_playing),
    ComingSoon(ComingSoonMoviesFragment.newInstance(), R.string.tab_name_coming_soon)
}