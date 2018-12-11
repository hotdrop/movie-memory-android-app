package jp.hotdrop.moviememory.data.remote

import androidx.annotation.CheckResult
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.remote.response.MovieResult
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    // ここは1つだけになるかも・・
    @GET("/movies/now-playing")
    @CheckResult
    fun nowPlaying(@Query("fromId") fromId: Int?): Single<List<MovieResult>>

    @GET("/movies/coming-soon")
    @CheckResult
    fun comingSoon(@Query("fromId") fromId: Int?): Flowable<List<MovieResult>>
}