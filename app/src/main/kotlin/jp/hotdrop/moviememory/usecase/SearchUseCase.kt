package jp.hotdrop.moviememory.usecase

import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.data.repository.CategoryRepository
import jp.hotdrop.moviememory.data.repository.SearchRepository
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.model.Suggestion
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.model.SearchCondition
import timber.log.Timber
import javax.inject.Inject

@Reusable
class SearchUseCase @Inject constructor(
        private val searchRepository: SearchRepository,
        private val categoryRepository: CategoryRepository
) {
    fun findCategories(): Single<List<Category>> =
            categoryRepository.findAll()
                    .subscribeOn(Schedulers.io())

    fun suggestion(): Flowable<List<Suggestion>> =
            searchRepository.suggestion()
                    .subscribeOn(Schedulers.io())

    fun save(suggestion: Suggestion): Completable =
            searchRepository.save(suggestion)
                    .subscribeOn(Schedulers.io())

    fun deleteSuggestions(): Completable =
            searchRepository.deleteSuggestion()
                    .subscribeOn(Schedulers.io())

    fun find(searchCondition: SearchCondition): Single<List<Movie>> {
        return when (searchCondition) {
            is SearchCondition.Keyword -> {
                Timber.d("キーワード検索をRepository経由で行う。")
                searchRepository.findMovies(searchCondition.keyword)
                        .subscribeOn(Schedulers.io())
            }
            is SearchCondition.Category -> {
                Timber.d("カテゴリー検索をRepository経由で行う。")
                searchRepository.findMovies(searchCondition.category)
                        .subscribeOn(Schedulers.io())
            }
            is SearchCondition.Favorite -> {
                Timber.d("お気に入り検索をRepository経由で行う。")
                searchRepository.findMoviesMoreThan(searchCondition.moreThanNum)
                        .subscribeOn(Schedulers.io())
            }
        }
    }

}