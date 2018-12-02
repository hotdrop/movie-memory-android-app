package jp.hotdrop.moviememory.data.repository

import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.MovieDatabase
import jp.hotdrop.moviememory.data.local.entity.toCategory
import jp.hotdrop.moviememory.model.Category
import timber.log.Timber
import javax.inject.Inject

class SearchDataRepository @Inject constructor(
        private val movieDatabase: MovieDatabase
): SearchRepository {

    override fun findCategories(): Single<List<Category>> =
            movieDatabase.findCategories()
                    .map { entities ->
                        Timber.d("取得したカテゴリー数 ${entities.size}")
                        entities.map {
                            Timber.d( "  id=${it.id} name=${it.name}")
                            it.toCategory()
                        }
                    }
}