package jp.hotdrop.moviememory.usecase

import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import jp.hotdrop.moviememory.data.repository.CategoryRepository
import jp.hotdrop.moviememory.model.Category
import timber.log.Timber
import javax.inject.Inject

@Reusable
class CategoryUseCase @Inject constructor(
        private val categoryRepository: CategoryRepository
) {
    fun findAll(): Single<List<Category>> =
            categoryRepository.findAllWithRegisterCount()
                    .map { categories ->
                        categories.filter { !it.isUnspecified() }
                    }
                    .subscribeOn(Schedulers.io())

    fun add(category: Category): Completable =
            categoryRepository.add(category)
                    .subscribeOn(Schedulers.io())

    fun update(category: Category): Completable =
            categoryRepository.update(category)
                    .subscribeOn(Schedulers.io())

    fun delete(category: Category): Completable =
            categoryRepository.delete(category)
                    .subscribeOn(Schedulers.io())

    fun integrate(fromCategory: Category, toCategory: Category): Completable =
            categoryRepository.integrate(fromCategory, toCategory)
                .subscribeOn(Schedulers.io())
}