package jp.hotdrop.moviememory.di.module

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import jp.hotdrop.moviememory.BuildConfig
import jp.hotdrop.moviememory.data.remote.AppJsonAdapterFactory
import jp.hotdrop.moviememory.data.remote.MovieApi
import jp.hotdrop.moviememory.di.InterceptorModule
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [InterceptorModule::class])
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptors: Set<@JvmSuppressWildcards Interceptor>): OkHttpClient =
            OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .apply {
                        interceptors.forEach { addInterceptor(it) }
                    }
                    .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.API_ENDPOINT)
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder()
                        .add(AppJsonAdapterFactory.INSTANCE)
                        .build()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }
}