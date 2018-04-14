package jp.hotdrop.moviememory.presentation

import android.support.v7.app.AppCompatActivity
import javax.inject.Inject

/**
 * Activity/Fragmentの画面遷移は全てここで行う
 */
class NavigationController @Inject constructor(
        private val activity: AppCompatActivity
) {
}