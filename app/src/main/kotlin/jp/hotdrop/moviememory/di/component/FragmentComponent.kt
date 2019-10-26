package jp.hotdrop.moviememory.di.component

import dagger.Subcomponent
import jp.hotdrop.moviememory.di.module.FragmentViewModelModule
import jp.hotdrop.moviememory.presentation.account.AccountFragment
import jp.hotdrop.moviememory.presentation.category.CategoryEditFragment
import jp.hotdrop.moviememory.presentation.movie.MoviesFragment
import jp.hotdrop.moviememory.presentation.movie.tab.TabMoviesFragment
import jp.hotdrop.moviememory.presentation.search.SearchFragment
import jp.hotdrop.moviememory.presentation.setting.SettingFragment

@Subcomponent(modules = [
    FragmentViewModelModule::class
])
interface FragmentComponent {

    fun inject(fragment: MoviesFragment)
    fun inject(fragment: TabMoviesFragment)

    fun inject(fragment: CategoryEditFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: SettingFragment)
    fun inject(fragment: AccountFragment)
}