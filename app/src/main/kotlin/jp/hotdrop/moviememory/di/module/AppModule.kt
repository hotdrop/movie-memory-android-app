package jp.hotdrop.moviememory.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import jp.hotdrop.moviememory.service.Firebase
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideFirebase(): Firebase = Firebase()
}