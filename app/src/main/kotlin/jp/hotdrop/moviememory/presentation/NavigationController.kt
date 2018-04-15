package jp.hotdrop.moviememory.presentation

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.presentation.movie.MoviesFragment
import javax.inject.Inject

/**
 * Activity/Fragmentの画面遷移は全てここで行う
 */
class NavigationController @Inject constructor(
        private val activity: AppCompatActivity
) {
    private val containerId: Int = R.id.content_view

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