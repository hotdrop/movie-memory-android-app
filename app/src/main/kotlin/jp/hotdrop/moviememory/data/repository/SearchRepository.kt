package jp.hotdrop.moviememory.data.repository

import androidx.annotation.CheckResult
import io.reactivex.Single
import jp.hotdrop.moviememory.model.Category

interface SearchRepository {

    @CheckResult
    fun findCategories(): Single<List<Category>>
}