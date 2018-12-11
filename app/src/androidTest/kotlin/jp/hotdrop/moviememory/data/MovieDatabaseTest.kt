package jp.hotdrop.moviememory.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import jp.hotdrop.moviememory.data.local.AppDatabase
import jp.hotdrop.moviememory.data.local.MovieDatabase
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset

@RunWith(AndroidJUnit4::class)
@LargeTest
class MovieDatabaseTest {

    // AACが使用するバックグラウンドExecutorを、各タスクを同期して実行する別物と入れ替える
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var appDb: AppDatabase
    private lateinit var movieDb: MovieDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        appDb = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        movieDb = MovieDatabase(appDb, appDb.movieDao())
    }

    @After
    fun closeDb() {
        appDb.close()
    }

    @Test
    fun getMoviesTest() {
        // 指定した期間のデータのみ取得対象であることをテスト
        val movieEntities = mutableListOf<MovieEntity>()
        (1..9).forEach {
            val nowEpoch = LocalDate.now().toEpochDay()
            val entity = createTestMovieEntity(it, nowEpoch)
            movieEntities.add(entity)
        }

        // ちょうど2ヶ月前はOK
        val nowDate = LocalDate.now()
        val startAt = nowDate.minusMonths(2L)
        val justTwoMonthAgoEpoch = startAt.toEpochDay()
        val justTwoMonthAgoEntity = createTestMovieEntity(10, justTwoMonthAgoEpoch)
        movieEntities.add(justTwoMonthAgoEntity)

        // 2ヶ月と1日前のデータは除外
        val inDustMovieEntities = movieEntities.toMutableList()
        val expiredEpoch = startAt.minusDays(1L).toEpochDay()
        val expiredEntity = createTestMovieEntity(11, expiredEpoch)
        inDustMovieEntities.add(expiredEntity)

        // 翌日のデータは除外
        val overEpoch = nowDate.plusDays(1L).toEpochDay()
        val overEntity = createTestMovieEntity(12, overEpoch)
        inDustMovieEntities.add(overEntity)

        movieDb.save(inDustMovieEntities)
        movieDb.findMoviesByBetween(startAt, nowDate)
                .test()
                .assertValue { results ->
                    assertTrue(" Error resultのサイズ=${results.size} movieのサイズ=${movieEntities.size}",
                            results.size == movieEntities.size)
                    assertMovieEntity(results, movieEntities)
                }
    }

    @Test
    fun orderByTest() {
        val movieEntities = mutableListOf<MovieEntity>()

        val nowDate = LocalDate.now()
        val secondReleaseEpoch = nowDate.minusDays(5L).toEpochDay()
        movieEntities.add(createTestMovieEntity(1, secondReleaseEpoch))

        val oldReleaseEpoch = nowDate.minusMonths(1L).toEpochDay()
        movieEntities.add(createTestMovieEntity(2, oldReleaseEpoch))

        val latestReleaseEpoch = nowDate.toEpochDay()
        movieEntities.add(createTestMovieEntity(3, latestReleaseEpoch))

        movieDb.save(movieEntities)

        movieDb.findMoviesByBetween(nowDate.minusMonths(5L), nowDate)
                .test()
                .assertValue { result ->
                    result[0].playingDate == latestReleaseEpoch &&
                    result[1].playingDate == secondReleaseEpoch &&
                    result[2].playingDate == oldReleaseEpoch
                }
    }

    private fun assertMovieEntity(o1: List<MovieEntity>, o2: List<MovieEntity>): Boolean {
        o1.zip(o2) { entity1, entity2 ->
            assertEquals(entity1.id, entity2.id)
            assertEquals(entity1.title, entity2.title)
            assertEquals(entity1.overview, entity2.overview)
            assertEquals(entity1.playingDate, entity2.playingDate)
        }
        return true
    }

    private fun createTestMovieEntity(id: Int, nowEpoch: Long) =
            MovieEntity(id,
                    "テスト$id",
                    1,
                    "カテゴリー1",
                    "概要",
                    "https://test.test",
                    nowEpoch,
                    "監督です。",
                    "https://www.google.co.jp",
                    "https://www.google.co.jp",
                    LocalDateTime.now().toInstant(ZoneOffset.UTC))
}