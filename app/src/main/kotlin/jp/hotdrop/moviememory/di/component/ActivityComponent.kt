package jp.hotdrop.moviememory.di.component

import dagger.Subcomponent
import jp.hotdrop.moviememory.di.module.ActivityModule
import jp.hotdrop.moviememory.di.module.ActivityViewModelModule
import jp.hotdrop.moviememory.di.module.FragmentModule
import jp.hotdrop.moviememory.presentation.MainActivity
import jp.hotdrop.moviememory.presentation.movie.detail.MovieDetailActivity
import jp.hotdrop.moviememory.presentation.movie.detail.MovieDetailEditActivity

@Subcomponent(modules = [
    ActivityModule::class,
    ActivityViewModelModule::class
])
interface ActivityComponent {

    fun inject(activity: MainActivity)
    fun inject(activity: MovieDetailActivity)
    fun inject(activity: MovieDetailEditActivity)

    fun plus(module: FragmentModule): FragmentComponent
}