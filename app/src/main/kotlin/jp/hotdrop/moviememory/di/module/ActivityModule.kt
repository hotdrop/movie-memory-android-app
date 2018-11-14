package jp.hotdrop.moviememory.di.module

import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule constructor(private var activity: AppCompatActivity) {
    @Provides
    fun provideActivity(): AppCompatActivity = activity
}