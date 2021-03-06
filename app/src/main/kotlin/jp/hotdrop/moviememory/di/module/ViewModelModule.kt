package jp.hotdrop.moviememory.di.module

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import jp.hotdrop.moviememory.di.ViewModelFactory

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}