package jp.hotdrop.moviememory.di.module

import dagger.Module
import dagger.Provides
import jp.hotdrop.moviememory.di.InterceptorModule
import jp.hotdrop.moviememory.di.NetworkInterceptorQualifier
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [InterceptorModule::class])
object NetworkModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideOkHttpClient(@NetworkInterceptorQualifier networkInterceptors: Set<@JvmSuppressWildcards Interceptor>): OkHttpClient =
            OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .apply { networkInterceptors.forEach { addNetworkInterceptor(it) } }
                    .build()
}