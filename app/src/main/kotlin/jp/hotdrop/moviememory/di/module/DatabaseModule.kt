package jp.hotdrop.moviememory.di.module

import androidx.room.Room
import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import jp.hotdrop.moviememory.data.local.dao.CategoryDao
import jp.hotdrop.moviememory.data.local.dao.MovieDao
import jp.hotdrop.moviememory.data.local.dao.MovieNoteDao
import jp.hotdrop.moviememory.data.local.dao.SuggestionDao
import jp.hotdrop.moviememory.data.local.database.AppDatabase
import javax.inject.Singleton

@Module
object DatabaseModule {

    private val MIGRATION_3_TO_4 = object: Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE movie ADD COLUMN originalAuthor TEXT")
            database.execSQL("ALTER TABLE movie ADD COLUMN distribution TEXT")
            database.execSQL("ALTER TABLE movie ADD COLUMN makeCountry TEXT")
            database.execSQL("ALTER TABLE movie ADD COLUMN makeYear INTEGER")
            database.execSQL("ALTER TABLE movie ADD COLUMN playTime INTEGER")
        }
    }

    @JvmStatic @Provides @Singleton
    fun provideDb(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "MovieMemory.db")
                    .addMigrations(MIGRATION_3_TO_4)
                    .fallbackToDestructiveMigrationFrom(1, 2)
                    .build()

    @JvmStatic @Provides @Singleton
    fun provideMovieDao(db: AppDatabase): MovieDao = db.movieDao()

    @JvmStatic @Provides @Singleton
    fun provideMovieNoteDao(db: AppDatabase): MovieNoteDao = db.movieNoteDao()

    @JvmStatic @Provides @Singleton
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()

    @JvmStatic @Provides @Singleton
    fun provideSuggestionDao(db: AppDatabase): SuggestionDao = db.suggestionDao()
}