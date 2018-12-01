package jp.hotdrop.moviememory.presentation

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivityMainBinding
import jp.hotdrop.moviememory.presentation.movie.MoviesFragment

class MainActivity: BaseActivity() {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getComponent().inject(this)

        initView()

        // 初期表示タブ
        if (savedInstanceState == null) {
            binding.navigation.selectedItemId = R.id.navigation_movie
        }
    }

    private fun initView() {
        binding.navigation.setOnNavigationItemReselectedListener { item ->
            item.isChecked = true
            when (item.itemId) {
                R.id.navigation_movie -> navigationToMovies()
                R.id.navigation_dashboard -> { }
                R.id.navigation_notifications -> { }
            }
        }

        // Tabとの間に線が入ってかっこ悪いのでトップ画面はelevationをoffにする
        supportActionBar?.let {
            it.elevation = 0f
        }
    }

    private fun navigationToMovies() {
        replaceFragment(MoviesFragment.newInstance())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.transaction {
            replace(R.id.content_view, fragment)
        }
    }
}
