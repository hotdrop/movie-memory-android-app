package jp.hotdrop.moviememory.presentation

import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import jp.hotdrop.moviememory.App
import jp.hotdrop.moviememory.di.ActivityComponent
import jp.hotdrop.moviememory.di.ActivityModule

abstract class BaseActivity: AppCompatActivity() {

    private val activityComponent by lazy {
        (application as App).getComponent().plus(ActivityModule(this))
    }

    fun getComponent(): ActivityComponent = activityComponent

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}