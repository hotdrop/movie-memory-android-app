package jp.hotdrop.moviememory.data.remote.api

import androidx.annotation.CheckResult
import io.reactivex.Single
import jp.hotdrop.moviememory.data.remote.response.MovieResponse

interface MovieApi {

    @CheckResult
    fun findAll(): Single<List<MovieResponse>>

    @CheckResult
    fun find(fromCreatedAt: Long): Single<List<MovieResponse>>
}