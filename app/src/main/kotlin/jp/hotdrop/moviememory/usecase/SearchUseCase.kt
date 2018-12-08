package jp.hotdrop.moviememory.usecase

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.data.repository.SearchRepository
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.model.Suggestion
import jp.hotdrop.moviememory.model.Movie
import javax.inject.Inject

class SearchUseCase @Inject constructor(
        private val repository: SearchRepository
) {
    fun findCategories(): Single<List<Category>> =
            repository.findCategories()
                    .subscribeOn(Schedulers.io())

    fun suggestion(): Flowable<List<Suggestion>> =
            repository.suggestion()
                    .subscribeOn(Schedulers.io())

    fun save(suggestion: Suggestion): Completable =
            repository.save(suggestion)
                    .subscribeOn(Schedulers.io())

    fun deleteSuggestions(): Completable =
            repository.deleteSuggestion()
                    .subscribeOn(Schedulers.io())

    fun findByKeyword(suggestion: Suggestion): Single<List<Movie>> =
            repository.findMovies(suggestion)
                    .subscribeOn(Schedulers.io())
}