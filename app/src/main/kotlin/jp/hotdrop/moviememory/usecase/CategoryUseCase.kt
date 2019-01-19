package jp.hotdrop.moviememory.usecase

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.data.repository.CategoryRepository
import jp.hotdrop.moviememory.model.Category
import javax.inject.Inject

class CategoryUseCase @Inject constructor(
        private val categoryRepository: CategoryRepository
) {
    fun flowable(): Flowable<List<Category>> =
            categoryRepository.categoriesWithRegisterCount()
                    .subscribeOn(Schedulers.io())

    fun add(category: Category): Completable =
            categoryRepository.add(category)
                    .subscribeOn(Schedulers.io())

    fun update(category: Category): Completable =
            categoryRepository.update(category)
                    .subscribeOn(Schedulers.io())

    fun integrate(fromCategory: Category, toCategory: Category): Completable =
            categoryRepository.integrate(fromCategory, toCategory)
                .subscribeOn(Schedulers.io())

    fun delete(category: Category): Completable =
            categoryRepository.delete(category)
                    .subscribeOn(Schedulers.io())
}