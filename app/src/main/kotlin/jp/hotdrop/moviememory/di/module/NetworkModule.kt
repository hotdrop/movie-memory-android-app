package jp.hotdrop.moviememory.di.module

import dagger.Module
import dagger.Provides
import jp.hotdrop.moviememory.di.InterceptorModule
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [InterceptorModule::class])
object NetworkModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideOkHttpClient(interceptors: Set<@JvmSuppressWildcards Interceptor>): OkHttpClient =
            OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .apply {
                        interceptors.forEach { addNetworkInterceptor(it) }
                    }
                    .build()
}