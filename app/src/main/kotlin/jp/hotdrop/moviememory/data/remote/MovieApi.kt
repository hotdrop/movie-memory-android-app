package jp.hotdrop.moviememory.data.remote

import android.support.annotation.CheckResult
import io.reactivex.Single
import jp.hotdrop.moviememory.data.remote.response.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("/movie/now_playing")
    @CheckResult
    fun getNowPraying(@Query("index") index: Int, @Query("offset") offset: Int): Single<MovieResponse>

    @GET("/movie/coming_soon")
    @CheckResult
    fun getComingSoon(@Query("index") index: Int, @Query("offset") offset: Int): Single<MovieResponse>

    // TODO searchとか入れる
}