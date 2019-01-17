package jp.hotdrop.moviememory.data.repository

import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.database.CategoryDatabase
import jp.hotdrop.moviememory.data.local.database.MovieDatabase
import jp.hotdrop.moviememory.data.local.entity.toCategory
import jp.hotdrop.moviememory.data.local.entity.toEntity
import jp.hotdrop.moviememory.model.Category
import timber.log.Timber
import javax.inject.Inject

@Reusable
class CategoryRepository @Inject constructor(
        private val categoryDatabase: CategoryDatabase,
        private val movieDatabase: MovieDatabase
) {

    fun findAll(): Single<List<Category>> {
        return categoryDatabase.findAll()
                .map { entities ->
                    entities.map { it.toCategory() }
                }
    }

    fun categoriesWithRegisterCount(): Flowable<List<Category>> {
        return categoryDatabase.flowable()
                .map { entities ->
                    entities.map { entity ->
                        val registerCnt = movieDatabase.countByCategory(entity.id)
                        entity.toCategory(registerCnt)
                    }
                }
    }

    fun add(category: Category): Completable {
        return Completable.create { emitter ->
            categoryDatabase.register(category.toEntity())
            emitter.onComplete()
        }
    }

    fun update(category: Category): Completable {
        return Completable.create { emitter ->
            categoryDatabase.update(category.toEntity())
            emitter.onComplete()
        }
    }

    fun delete(category: Category): Completable {
        return Completable.create { emitter ->
            categoryDatabase.delete(category.toEntity())
            emitter.onComplete()
        }
    }

    fun integrate(fromCategory: Category, toCategory: Category): Completable {
        return Completable.create { emitter ->
            Timber.d("登録されている映画情報を ${fromCategory.name} から ${toCategory.name} にリプレイスします。")
            movieDatabase.integrateCategory(fromCategory.toEntity(), toCategory.toEntity())
            emitter.onComplete()
        }
    }
}