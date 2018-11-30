package jp.hotdrop.moviememory.di.component

import dagger.Subcomponent
import jp.hotdrop.moviememory.di.module.FragmentModule
import jp.hotdrop.moviememory.di.module.FragmentViewModelModule
import jp.hotdrop.moviememory.presentation.movie.MoviesFragment
import jp.hotdrop.moviememory.presentation.movie.tab.TabMoviesFragment

@Subcomponent(modules = [
    FragmentModule::class,
    FragmentViewModelModule::class
])
interface FragmentComponent {
    fun inject(fragment: MoviesFragment)
    fun inject(fragment: TabMoviesFragment)
}