package jp.hotdrop.moviememory.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
internal class InterceptorModule {

    @Singleton
    @Provides
    @IntoSet
    fun provideStetho(): Interceptor = StethoInterceptor()

    @Singleton
    @Provides
    @IntoSet
    fun provideHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}