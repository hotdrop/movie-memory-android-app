package jp.hotdrop.moviememory.presentation

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.presentation.movie.MoviesFragment
import jp.hotdrop.moviememory.presentation.movie.detail.MovieDetailActivity
import javax.inject.Inject

/**
 * Activity/Fragmentの画面遷移は全てここで行う
 * このパターンがいいと思っていないのだが、Activity間の分離を行えるメリットがあり
 * 肥大化する懸念も比較的小さなアプリなら大丈夫なのでこのアプリは画面遷移を集約管理する。
 */
class NavigationController @Inject constructor(
        private val activity: AppCompatActivity
) {
    private val containerId: Int = R.id.content_view

    fun navigateToMovieDetail(movieId: Int) {
        MovieDetailActivity.start(activity, movieId)
    }

    fun navigateToMovies() {
        replaceFragment(MoviesFragment.newInstance())
    }

    private fun replaceFragment(fragment: Fragment) {
        activity.supportFragmentManager
                .beginTransaction()
                .replace(containerId, fragment)
                .commit()
    }
}