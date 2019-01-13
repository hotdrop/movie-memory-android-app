package jp.hotdrop.moviememory.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import jp.hotdrop.moviememory.data.local.database.AppDatabase
import jp.hotdrop.moviememory.data.local.database.MovieNoteDatabase
import jp.hotdrop.moviememory.data.local.entity.MovieNoteEntity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDate

@RunWith(AndroidJUnit4::class)
@LargeTest
class MovieNoteDatabaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var appDb: AppDatabase
    private lateinit var movieNoteDb: MovieNoteDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        appDb = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        movieNoteDb = MovieNoteDatabase(appDb.movieNoteDao())
    }

    @After
    fun closeDb() {
        appDb.close()
    }

    @Test
    fun getMovieNoteTest() {
        val firstDataId = 1200L
        val firstData = createLocalMovieInfoEntity(firstDataId)
        movieNoteDb.save(firstData)

        val secondDataId = 1300L
        val secondData = createLocalMovieInfoEntity(secondDataId)
        movieNoteDb.save(secondData)

        val resultOne = movieNoteDb.find(firstDataId)
        assert(resultOne == firstData)

        val resultTwo = movieNoteDb.find(secondDataId)
        assert(resultTwo == secondData)
    }

    private fun createLocalMovieInfoEntity(id: Long) =
            MovieNoteEntity(id,
                    1,
                    LocalDate.parse("2018-05-20").toEpochDay(),
                    "Dummy Place",
                    "Memo dummy"
            )
}