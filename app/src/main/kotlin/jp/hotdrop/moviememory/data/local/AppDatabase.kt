package jp.hotdrop.moviememory.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
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