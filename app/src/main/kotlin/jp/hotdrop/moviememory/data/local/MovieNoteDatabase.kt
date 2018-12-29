package jp.hotdrop.moviememory.data.local

import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.dao.MovieNoteDao
import jp.hotdrop.moviememory.data.local.entity.MovieNoteEntity
import jp.hotdrop.moviememory.model.Suggestion
import javax.inject.Inject

class MovieNoteDatabase @Inject constructor(
        private val dao: MovieNoteDao
) {

    fun find(id: Long): MovieNoteEntity =
            dao.select(id)

    fun find(keyword: String): Single<List<MovieNoteEntity>> =
            dao.select(keyword)

    fun findMoreThan(favoriteNum: Int): Single<List<MovieNoteEntity>> =
            dao.selectMoreThan(favoriteNum)

    fun save(entity: MovieNoteEntity) {
        dao.insert(entity)
    }
}