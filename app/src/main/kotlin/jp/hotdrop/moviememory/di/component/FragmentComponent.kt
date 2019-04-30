package jp.hotdrop.moviememory.di.component

import dagger.Subcomponent
import jp.hotdrop.moviememory.di.module.FragmentViewModelModule
import jp.hotdrop.moviememory.presentation.category.CategoryFragment
import jp.hotdrop.moviememory.presentation.movie.MoviesFragment
import jp.hotdrop.moviememory.presentation.movie.edit.MovieEditFragment
import jp.hotdrop.moviememory.presentation.movie.tab.TabMoviesFragment
import jp.hotdrop.moviememory.presentation.search.SearchFragment
import jp.hotdrop.moviememory.presentation.setting.SettingFragment

@Subcomponent(modules = [
    FragmentViewModelModule::class
])
interface FragmentComponent {

    fun inject(fragment: MoviesFragment)
    fun inject(fragment: TabMoviesFragment)

    fun inject(fragment: MovieEditFragment)

    fun inject(fragment: CategoryFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: SettingFragment)
}