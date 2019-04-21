package jp.hotdrop.moviememory.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import jp.hotdrop.moviememory.di.module.*
import jp.hotdrop.moviememory.presentation.common.MovieMemoryGlideModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ViewModelModule::class,
    LocalStorageModule::class,
    NetworkModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun appContext(appContext: Context): Builder

        fun build(): AppComponent
    }

    fun plus(): ActivityComponent
    fun plus(glideModule: MovieMemoryGlideModule)
}