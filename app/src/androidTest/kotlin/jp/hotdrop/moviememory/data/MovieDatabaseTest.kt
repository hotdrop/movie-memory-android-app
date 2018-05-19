package jp.hotdrop.moviememory.data

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import jp.hotdrop.moviememory.data.local.AppDatabase
import jp.hotdrop.moviememory.data.local.MovieDatabase
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset

@RunWith(AndroidJUnit4::class)
class MovieDatabaseTest {

    private lateinit var appDb: AppDatabase
    private lateinit var movieDb: MovieDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getContext()
        appDb = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        movieDb = MovieDatabase(appDb, appDb.movieDao())
    }

    @After
    fun closeDb() {
        appDb.close()
    }

    @Test
    fun saveTest() {
        val movieEntites = (1..10).map {
            val nowEpoch = LocalDate.now().toEpochDay()
            createTestMovieEntity(it, nowEpoch)
        }
        movieDb.save(movieEntites)
        val resultFromDb = movieDb.getNowPlayingMovies()
        assert(movieEntites == resultFromDb)
    }

    private fun createTestMovieEntity(id: Int, nowEpoch: Long) =
            MovieEntity(id,
                    "テスト$id",
                    "概要",
                    "https://test.test",
                    nowEpoch,
                    "監督です。",
                    "https://www.google.co.jp",
                    "https://www.youtube.test",
                    LocalDateTime.now().toInstant(ZoneOffset.UTC))
}