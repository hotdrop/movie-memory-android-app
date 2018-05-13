package jp.hotdrop.moviememory.di.component

import dagger.Component
import jp.hotdrop.moviememory.di.module.*
import javax.inject.Singleton

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
}