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

    fun findAllWithRegisterCount(): Single<List<Category>> {
        return categoryDatabase.findAll()
                .map { entities ->
                    entities.map { entity ->
                        val registerCnt = entity.id?.let {movieDatabase.countByCategory(it)} ?: 0
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
            // ここのカテゴリーIDは必ず存在するので!!をつける
            movieDatabase.updateCategory(category.id!!, Category.UNSPECIFIED_ID)
            categoryDatabase.delete(category.id)
            emitter.onComplete()
        }
    }

    fun integrate(fromCategory: Category, toCategory: Category): Completable {
        return Completable.create { emitter ->
            Timber.d("登録されている映画情報を ${fromCategory.name} から ${toCategory.name} にリプレイスします。")
            // ここのカテゴリーIDは必ず存在するので!!をつける
            movieDatabase.updateCategory(fromCategory.id!!, toCategory.id!!)
            emitter.onComplete()
        }
    }
}