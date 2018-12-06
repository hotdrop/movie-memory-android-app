package jp.hotdrop.moviememory.usecase

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.data.repository.SearchRepository
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.model.SearchKeyword
import jp.hotdrop.moviememory.model.Movie
import javax.inject.Inject

class SearchUseCase @Inject constructor(
        private val repository: SearchRepository
) {
    fun findCategories(): Single<List<Category>> =
            repository.findCategories()
                    .subscribeOn(Schedulers.io())

    fun suggestion(): Flowable<List<SearchKeyword>> =
            repository.suggestion()
                    .subscribeOn(Schedulers.io())

    fun saveSuggestion(searchKeyword: SearchKeyword): Completable =
            repository.saveSuggestion(searchKeyword)
                    .subscribeOn(Schedulers.io())

    fun deleteSuggestion(): Completable =
            repository.deleteSuggestion()
                    .subscribeOn(Schedulers.io())

    fun findByKeyword(searchKeyword: SearchKeyword): Single<List<Movie>> =
            repository.findMovies(searchKeyword)
                    .subscribeOn(Schedulers.io())
}