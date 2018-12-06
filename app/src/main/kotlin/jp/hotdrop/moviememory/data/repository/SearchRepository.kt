package jp.hotdrop.moviememory.data.repository

import androidx.annotation.CheckResult
import io.reactivex.Single
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.model.SearchKeyword
import jp.hotdrop.moviememory.model.Movie

interface SearchRepository {

    @CheckResult
    fun findCategories(): Single<List<Category>>

    @CheckResult
    fun findMovies(searchKeyword: SearchKeyword): Single<List<Movie>>
}