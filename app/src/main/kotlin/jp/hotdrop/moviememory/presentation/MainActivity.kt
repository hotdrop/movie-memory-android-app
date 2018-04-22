package jp.hotdrop.moviememory.presentation

import android.databinding.DataBindingUtil
import android.os.Bundle
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity: BaseActivity() {

    @Inject
    lateinit var navigationController: NavigationController

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
                R.id.navigation_movie -> navigationController.navigateToMovies()
                R.id.navigation_dashboard -> { }
                R.id.navigation_notifications -> { }
            }
        }
    }

}
