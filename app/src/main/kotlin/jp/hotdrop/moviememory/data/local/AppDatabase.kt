package jp.hotdrop.moviememory.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jp.hotdrop.moviememory.data.local.dao.MovieDao
import jp.hotdrop.moviememory.data.local.entity.Converters
import jp.hotdrop.moviememory.data.local.entity.LocalMovieInfoEntity
import jp.hotdrop.moviememory.data.local.entity.MovieEntity

@Database(
        entities = [
            (MovieEntity::class),
            (LocalMovieInfoEntity::class)
        ],
        version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
}