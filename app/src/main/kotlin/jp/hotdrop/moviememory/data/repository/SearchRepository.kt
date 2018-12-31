package jp.hotdrop.moviememory.data.repository

import androidx.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.model.Suggestion
import jp.hotdrop.moviememory.model.Movie

interface SearchRepository {

    @CheckResult
    fun suggestion(): Flowable<List<Suggestion>>

    @CheckResult
    fun save(suggestion: Suggestion): Completable

    @CheckResult
    fun deleteSuggestion(): Completable

    @CheckResult
    fun findMovies(keyword: String): Single<List<Movie>>

    @CheckResult
    fun findMovies(category: Category): Single<List<Movie>>

    @CheckResult
    fun findMoviesMoreThan(favoriteNum: Int): Single<List<Movie>>
}