package jp.hotdrop.moviememory.data.repository

import androidx.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.model.SearchKeyword
import jp.hotdrop.moviememory.model.Movie

interface SearchRepository {

    @CheckResult
    fun findCategories(): Single<List<Category>>

    @CheckResult
    fun suggestion(): Flowable<List<SearchKeyword>>

    @CheckResult
    fun saveSuggestion(keyword: SearchKeyword): Completable

    @CheckResult
    fun deleteSuggestion(): Completable

    @CheckResult
    fun findMovies(searchKeyword: SearchKeyword): Single<List<Movie>>
}