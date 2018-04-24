package jp.hotdrop.moviememory.di.component

import dagger.Component
import jp.hotdrop.moviememory.di.module.*
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    RepositoryModule::class,
    NetworkModule::class,
    ViewModelModule::class
])
interface AppComponent {
    fun plus(module: ActivityModule): ActivityComponent
}