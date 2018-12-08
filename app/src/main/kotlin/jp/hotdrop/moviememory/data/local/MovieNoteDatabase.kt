package jp.hotdrop.moviememory.data.local

import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.dao.MovieNoteDao
import jp.hotdrop.moviememory.data.local.entity.MovieNoteEntity
import jp.hotdrop.moviememory.model.Suggestion
import javax.inject.Inject

class MovieNoteDatabase @Inject constructor(
        private val dao: MovieNoteDao
) {

    fun find(id: Int): MovieNoteEntity =
            dao.select(id)

    fun find(suggestion: Suggestion): Single<List<MovieNoteEntity>> =
            dao.select(suggestion.keyword)

    fun save(entity: MovieNoteEntity) {
        dao.insert(entity)
    }
}