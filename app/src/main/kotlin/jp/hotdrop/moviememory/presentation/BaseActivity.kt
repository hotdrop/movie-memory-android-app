package jp.hotdrop.moviememory.presentation

import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

abstract class BaseActivity: AppCompatActivity() {

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}