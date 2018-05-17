package jp.hotdrop.moviememory.data.remote

import android.support.annotation.CheckResult
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.remote.response.MovieResult
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("/movie/now_playing")
    @CheckResult
    fun getNowPraying(@Query("index") index: Int, @Query("offset") offset: Int): Single<List<MovieResult>>

    @GET("/movie/coming_soon")
    @CheckResult
    fun getComingSoon(@Query("index") index: Int, @Query("offset") offset: Int): Flowable<List<MovieResult>>

    // TODO searchとか入れる
}