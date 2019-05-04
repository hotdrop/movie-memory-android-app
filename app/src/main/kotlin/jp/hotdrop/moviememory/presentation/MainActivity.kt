package jp.hotdrop.moviememory.presentation

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.snackbar.Snackbar
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivityMainBinding
import jp.hotdrop.moviememory.di.component.component
import jp.hotdrop.moviememory.presentation.account.AccountFragment
import jp.hotdrop.moviememory.presentation.category.CategoryEditFragment
import jp.hotdrop.moviememory.presentation.movie.MoviesFragment
import jp.hotdrop.moviememory.presentation.search.SearchFragment
import jp.hotdrop.moviememory.presentation.setting.SettingFragment
import jp.hotdrop.moviememory.service.Firebase
import javax.inject.Inject

class MainActivity: BaseActivity() {

    @Inject
    lateinit var firebase: Firebase

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // TODO ここもViewModel-Repository経由にした方がいい
        firebase.loginByAnonymous {
            Snackbar.make(binding.snackbarArea, "ログインに失敗しました。", Snackbar.LENGTH_LONG).show()
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
                    replaceFragment(CategoryEditFragment.newInstance())
                }
                R.id.navigation_search -> {
                    binding.toolbar.title = getString(R.string.title_search)
                    replaceFragment(SearchFragment.newInstance())
                }
                R.id.navigation_setting -> {
                    binding.toolbar.title = getString(R.string.title_setting)
                    replaceFragment(SettingFragment.newInstance())
                }
                R.id.navigation_account -> {
                    binding.toolbar.title = getString(R.string.title_account)
                    replaceFragment(AccountFragment.newInstance())
                }
            }
            false
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.content_view, fragment)
        }
    }
}
