package jp.hotdrop.moviememory.di.module

import androidx.room.Room
import android.content.Context
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import jp.hotdrop.moviememory.data.local.AppDatabase
import jp.hotdrop.moviememory.data.local.MovieDatabase
import jp.hotdrop.moviememory.data.local.MovieNoteDatabase
import jp.hotdrop.moviememory.data.local.SuggestionDatabase
import jp.hotdrop.moviememory.data.local.dao.MovieDao
import jp.hotdrop.moviememory.data.local.dao.MovieNoteDao
import jp.hotdrop.moviememory.data.local.dao.SuggestionDao
import javax.inject.Singleton

@Module
open class DatabaseModule {

    @Singleton
    @Provides
    open fun provideDb(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "MovieMemory.db")
                    .fallbackToDestructiveMigration()
                    .build()

    @Provides
    @Singleton
    fun provideMovieDao(db: AppDatabase): MovieDao = db.movieDao()

    @Singleton
    @Provides
    fun provideMovieDatabase(db: AppDatabase, dao: MovieDao): MovieDatabase =
            MovieDatabase(db, dao)

    @Provides
    @Singleton
    fun provideMovieNoteDao(db: AppDatabase): MovieNoteDao = db.movieNoteDao()

    @Singleton
    @Provides
    fun provideMovieNoteDatabase(dao: MovieNoteDao): MovieNoteDatabase =
            MovieNoteDatabase(dao)

    @Provides
    @Singleton
    fun provideSuggestionDao(db: AppDatabase): SuggestionDao = db.suggestionDao()

    @Singleton
    @Provides
    fun provideSuggestionDatabase(dao: SuggestionDao): SuggestionDatabase =
            SuggestionDatabase(dao)
}