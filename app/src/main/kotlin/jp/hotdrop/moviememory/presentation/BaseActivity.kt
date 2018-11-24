package jp.hotdrop.moviememory.presentation

import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import jp.hotdrop.moviememory.MovieMemoryApp
import jp.hotdrop.moviememory.di.component.ActivityComponent
import jp.hotdrop.moviememory.di.module.ActivityModule

abstract class BaseActivity: AppCompatActivity() {

    private val activityComponent by lazy {
        (application as MovieMemoryApp).getComponent().plus(ActivityModule(this))
    }

    fun getComponent(): ActivityComponent = activityComponent

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}