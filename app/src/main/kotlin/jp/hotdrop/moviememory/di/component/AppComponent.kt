package jp.hotdrop.moviememory.di.component

import dagger.Component
import jp.hotdrop.moviememory.di.module.*
import jp.hotdrop.moviememory.presentation.parts.MovieMemoryGlideModule
import javax.inject.Singleton

/**
 * TODO いずれandroid.supportにする
 */
@Singleton
@Component(modules = [
    AppModule::class,
    ViewModelModule::class,
    UseCaseModule::class,
    RepositoryModule::class,
    DatabaseModule::class,
    NetworkModule::class
])
interface AppComponent {
    fun plus(module: ActivityModule): ActivityComponent
    fun plus(glideModule: MovieMemoryGlideModule)
}