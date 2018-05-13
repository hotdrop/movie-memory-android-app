package jp.hotdrop.moviememory.data.remote

import com.squareup.moshi.Moshi
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class MockHttpClient(
        private val url: HttpUrl
) {

    private val httpClient = OkHttpClient.Builder().build()
    private val mockRetrofit: Retrofit by lazy {
        Retrofit.Builder()
                .client(httpClient)
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder()
                        .add(AppJsonAdapterFactory.INSTANCE)
                        // TODO DateTimeの解決をしないとダメかも
                        .build()))
                .build()
    }

    fun movieApi(): MovieApi = mockRetrofit.create(MovieApi::class.java)
}