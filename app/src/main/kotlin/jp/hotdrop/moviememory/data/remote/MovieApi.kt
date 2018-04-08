package jp.hotdrop.moviememory.data.remote

import android.support.annotation.CheckResult
import io.reactivex.Single
import jp.hotdrop.moviememory.data.remote.response.MovieEntity
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface MovieApi {

    @GET("/movie/now_playing")
    @CheckResult
    fun getNowPraying(@Query("page") page: Int, @Query("offset") offset: Int): Single<List<MovieEntity>>

    @GET("/movie/coming_soon")
    @CheckResult
    fun getComingSoon(@Query("page") page: Int, @Query("offset") offset: Int): Single<List<MovieEntity>>

    // TODO searchとか入れる
}