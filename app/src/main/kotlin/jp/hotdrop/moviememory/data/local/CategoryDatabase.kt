package jp.hotdrop.moviememory.data.local

import dagger.Reusable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.dao.CategoryDao
import jp.hotdrop.moviememory.data.local.entity.CategoryEntity
import jp.hotdrop.moviememory.model.Category
import timber.log.Timber
import javax.inject.Inject

@Reusable
class CategoryDatabase @Inject constructor(
        private val database: AppDatabase,
        private val dao: CategoryDao
) {

    fun register(name: String): Long {
        if (name.isEmpty()) {
            dao.select(Category.UNSPECIFIED_NAME)?.let {
                Timber.d("カテゴリー登録 未指定は登録済")
                return it.id
            } ?: kotlin.run {
                Timber.d("カテゴリー登録 未指定 を登録します。")
                var id: Long = 0
                database.runInTransaction {
                    id = dao.insert(CategoryEntity(Category.UNSPECIFIED_ID, Category.UNSPECIFIED_NAME))
                }
                return id
            }
        } else {
            dao.select(name)?.let {
                Timber.d("カテゴリー登録 $name は登録済。")
                return it.id
            } ?: kotlin.run {
                Timber.d("カテゴリー登録 $name を登録します。")
                var id: Long = 0
                database.runInTransaction {
                    id = dao.insert(CategoryEntity(name = name))
                }
                return id
            }
        }
    }

    fun find(id: Long): CategoryEntity = dao.select(id)

    fun findAll(): Single<List<CategoryEntity>> = dao.selectAll()
}