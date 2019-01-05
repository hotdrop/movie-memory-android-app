package jp.hotdrop.moviememory.data.local

import dagger.Reusable
import io.reactivex.Flowable
import jp.hotdrop.moviememory.data.local.dao.SuggestionDao
import jp.hotdrop.moviememory.data.local.entity.SuggestionEntity
import javax.inject.Inject

@Reusable
class SuggestionDatabase @Inject constructor(
        private val dao: SuggestionDao
) {
    fun suggestion(): Flowable<List<SuggestionEntity>> =
            dao.suggestions()

    fun save(suggestion: SuggestionEntity) {
        dao.insert(suggestion)
    }

    fun delete() {
        dao.delete()
    }
}