package jp.hotdrop.moviememory.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jp.hotdrop.moviememory.data.local.dao.CategoryDao
import jp.hotdrop.moviememory.data.local.dao.MovieDao
import jp.hotdrop.moviememory.data.local.dao.MovieNoteDao
import jp.hotdrop.moviememory.data.local.dao.SuggestionDao
import jp.hotdrop.moviememory.data.local.entity.*

@Database(
        entities = [
            (MovieEntity::class),
            (MovieNoteEntity::class),
            (CategoryEntity::class),
            (SuggestionEntity::class)
        ],
        version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movieNoteDao(): MovieNoteDao
    abstract fun categoryDao(): CategoryDao
    abstract fun suggestionDao(): SuggestionDao
}