package jp.hotdrop.moviememory.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import jp.hotdrop.moviememory.data.local.database.AppDatabase
import jp.hotdrop.moviememory.data.local.database.MovieDatabase
import jp.hotdrop.moviememory.data.local.entity.CategoryEntity
import jp.hotdrop.moviememory.data.local.entity.MovieEntity
import jp.hotdrop.moviememory.model.AppDate
import jp.hotdrop.moviememory.model.AppDateTime
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith

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
        movieDb = MovieDatabase(appDb, appDb.movieDao(), appDb.categoryDao())
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
            val nowEpoch = AppDate.nowEpochDay()
            val entity = createTestMovieEntity(it.toLong(), nowEpoch)
            movieEntities.add(entity)
        }

        // ちょうど2ヶ月前はOK
        val nowDate = AppDate()
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

        val nowDate = AppDate()
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

    @Test
    fun saveTest() {
        val nowDate = AppDate.nowEpochDay()
        val movies = (0..2).map { it.toLong() }
                .map { createTestMovieEntity(it, nowDate) }
        movieDb.save(movies)

        movieDb.findMovies("テスト")
                .test()
                .assertValue { moviesFromDB ->
                    val casts = moviesFromDB[0].casts
                    assertEquals("値がおかしいです。casts[0] = ${casts!![0]}", "a", casts[0])
                    assertEquals("値がおかしいです。casts[1] = ${casts[1]}", "b", casts[1])
                    assertEquals("値がおかしいです。casts[2] = ${casts[2]}", "c", casts[2])
                    true
                }

    }

    @Test
    fun integrateCategoryTest() {
        val nowDate = AppDate.nowEpochDay()
        val createAt = AppDateTime().toEpochMilli()

        val movies = mutableListOf<MovieEntity>()
        movies.add(MovieEntity(1, "テスト1", 1, "概要", "https://test.test", nowDate, null, null, null, null, createAt, null, null, null, null, null))
        movies.add(MovieEntity(2, "テスト2", 1, "概要", "https://test.test", nowDate, null, null, null, null, createAt, null, null, null, null, null))
        movies.add(MovieEntity(3, "テスト3", 2, "概要", "https://test.test", nowDate, null, null, null, null, createAt, null, null, null, null, null))
        movies.add(MovieEntity(4, "テスト4", 2, "概要", "https://test.test", nowDate, null, null, null, null, createAt, null, null, null, null, null))
        movieDb.save(movies)

        val fromCategory = CategoryEntity(id = 2, name = "消えるカテゴリー")
        val toCategory = CategoryEntity(id = 1, name = "こっちに統合")
        movieDb.updateCategory(fromCategory.id!!, toCategory.id!!)

        val cnt1 = movieDb.countByCategory(1)
        assertEquals(4, cnt1)
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

    private fun createTestMovieEntity(id: Long, nowEpoch: Long) =
            MovieEntity(id,
                    "テスト$id",
                    1,
                    "概要",
                    "https://test.test",
                    nowEpoch,
                    "監督です。",
                    arrayListOf("a", "b", "c"),
                    "https://www.google.co.jp",
                    "https://www.google.co.jp",
                    AppDateTime.nowEpoch(),
                    null,
                    null,
                    null,
                    null,
                    null)
}