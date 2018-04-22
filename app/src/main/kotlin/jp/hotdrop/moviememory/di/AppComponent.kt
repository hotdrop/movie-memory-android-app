package jp.hotdrop.moviememory.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    ViewModelModule::class
])
interface AppComponent {
    fun plus(module: ActivityModule): ActivityComponent
}