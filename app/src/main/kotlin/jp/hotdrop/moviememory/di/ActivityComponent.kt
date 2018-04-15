package jp.hotdrop.moviememory.di

import dagger.Subcomponent
import jp.hotdrop.moviememory.presentation.MainActivity

@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(activity: MainActivity)

    fun plus(module: FragmentModule): FragmentComponent
}