package jp.hotdrop.moviememory.data.repository

import androidx.annotation.CheckResult
import io.reactivex.Single
import jp.hotdrop.moviememory.model.Category

interface CategoryRepository {
    @CheckResult
    fun findAll(): Single<List<Category>>
}