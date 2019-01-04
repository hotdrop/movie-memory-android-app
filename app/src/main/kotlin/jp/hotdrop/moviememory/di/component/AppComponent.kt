package jp.hotdrop.moviememory.di.component

import dagger.Component
import jp.hotdrop.moviememory.di.module.*
import jp.hotdrop.moviememory.presentation.parts.MovieMemoryGlideModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    ViewModelModule::class,
    DatabaseModule::class,
    NetworkModule::class
])
interface AppComponent {
    fun plus(): ActivityComponent
    fun plus(glideModule: MovieMemoryGlideModule)
}