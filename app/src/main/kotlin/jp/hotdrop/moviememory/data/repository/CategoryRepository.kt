package jp.hotdrop.moviememory.data.repository

import dagger.Reusable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.database.CategoryDatabase
import jp.hotdrop.moviememory.data.local.entity.toCategory
import jp.hotdrop.moviememory.model.Category
import javax.inject.Inject

@Reusable
class CategoryRepository @Inject constructor(
        private val categoryDatabase: CategoryDatabase
) {
    fun findAll(): Single<List<Category>> {
        return categoryDatabase.findAll()
                .map { entities ->
                    entities.map { it.toCategory() }
                }
    }
}