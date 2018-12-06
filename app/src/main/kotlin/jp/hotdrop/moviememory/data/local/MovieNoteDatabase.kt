package jp.hotdrop.moviememory.data.local

import androidx.room.RoomDatabase
import io.reactivex.Single
import jp.hotdrop.moviememory.data.local.dao.MovieNoteDao
import jp.hotdrop.moviememory.data.local.entity.MovieNoteEntity
import jp.hotdrop.moviememory.model.SearchKeyword
import javax.inject.Inject

class MovieNoteDatabase @Inject constructor(
        private val database: RoomDatabase,
        private val dao: MovieNoteDao
) {

    fun find(id: Int): MovieNoteEntity =
            dao.select(id)

    fun find(searchKeyword: SearchKeyword): Single<List<MovieNoteEntity>> =
            dao.select(searchKeyword.value)

    fun save(entity: MovieNoteEntity) {
        database.runInTransaction {
            dao.insert(entity)
        }
    }
}