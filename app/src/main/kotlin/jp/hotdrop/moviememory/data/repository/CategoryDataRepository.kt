package jp.hotdrop.moviememory.data.repository

import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.CategoryDatabase
import jp.hotdrop.moviememory.data.local.entity.toCategory
import jp.hotdrop.moviememory.model.Category
import timber.log.Timber
import javax.inject.Inject

class CategoryDataRepository @Inject constructor(
        private val categoryDatabase: CategoryDatabase
): CategoryRepository {

    override fun findAll(): Single<List<Category>> {
        return categoryDatabase.findAll()
                .map { entities ->
                    Timber.d("取得したカテゴリー数 ${entities.size}")
                    entities.map {
                        Timber.d( "  id=${it.id} name=${it.name}")
                        it.toCategory()
                    }
                }
    }
}