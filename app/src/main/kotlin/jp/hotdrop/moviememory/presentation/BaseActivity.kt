package jp.hotdrop.moviememory.presentation

import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import jp.hotdrop.moviememory.MovieMemoryApp
import jp.hotdrop.moviememory.di.component.ActivityComponent
import javax.inject.Inject

abstract class BaseActivity: AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}