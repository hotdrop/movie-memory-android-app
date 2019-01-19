package jp.hotdrop.moviememory.presentation

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import com.google.android.material.snackbar.Snackbar
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivityMainBinding
import jp.hotdrop.moviememory.di.component.component
import jp.hotdrop.moviememory.presentation.category.CategoryFragment
import jp.hotdrop.moviememory.presentation.movie.MoviesFragment
import jp.hotdrop.moviememory.presentation.search.SearchFragment
import jp.hotdrop.moviememory.presentation.setting.SettingFragment
import jp.hotdrop.moviememory.service.Firebase
import javax.inject.Inject

class MainActivity: BaseActivity() {

    @Inject
    lateinit var firebase: Firebase

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        firebase.login {
            Snackbar.make(binding.snackbarArea, "Firebaseのログインに失敗しました。", Snackbar.LENGTH_LONG).show()
        }

        initView()

        // 初期表示タブ
        if (savedInstanceState == null) {
            binding.navigation.selectedItemId = R.id.navigation_movie
        }
    }

    private fun initView() {

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.navigation.setOnNavigationItemSelectedListener { item ->
            item.isChecked = true
            when (item.itemId) {
                R.id.navigation_movie -> {
                    binding.toolbar.title = getString(R.string.title_movies)
                    replaceFragment(MoviesFragment.newInstance())
                }
                R.id.navigation_category -> {
                    binding.toolbar.title = getString(R.string.title_category)
                    replaceFragment(CategoryFragment.newInstance())
                }
                R.id.navigation_search -> {
                    binding.toolbar.title = getString(R.string.title_search)
                    replaceFragment(SearchFragment.newInstance())
                }
                R.id.navigation_setting -> {
                    binding.toolbar.title = getString(R.string.title_setting)
                    replaceFragment(SettingFragment.newInstance())
                }
            }
            false
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.transaction {
            replace(R.id.content_view, fragment)
        }
    }
}
