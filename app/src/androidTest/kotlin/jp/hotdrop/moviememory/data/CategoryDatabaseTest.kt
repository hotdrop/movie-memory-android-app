package jp.hotdrop.moviememory.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import jp.hotdrop.moviememory.data.local.database.AppDatabase
import jp.hotdrop.moviememory.data.local.database.CategoryDatabase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertTrue

@RunWith(AndroidJUnit4::class)
class CategoryDatabaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: CategoryDatabase
    private lateinit var appDb: AppDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        appDb = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        db = CategoryDatabase(appDb, appDb.categoryDao())
    }

    @After
    fun closeDb() {
        appDb.close()
    }

    @Test
    fun registerTest() {
        val name1 = "test1"
        val name2 = "test2"
        val name3 = "test3"
        val name4 = ""

        val name1Id = db.register(name1)
        db.register(name1)
        val name2Id = db.register(name2)
        val name3Id = db.register(name3)
        val name4Id = db.register(name4)

        val existName1Id = db.register(name1)
        val existName4Id = db.register(name4)
        val existName3Id = db.register(name3)
        val existName2Id = db.register(name2)

        assertTrue("登録時のID=$name1Id 再取得時のID=$existName1Id", name1Id == existName1Id)
        assertTrue("登録時のID=$name2Id 再取得時のID=$existName2Id", name2Id == existName2Id)
        assertTrue("登録時のID=$name3Id 再取得時のID=$existName3Id", name3Id == existName3Id)
        assertTrue("登録時のID=$name4Id 再取得時のID=$existName4Id", name4Id == existName4Id)

        db.findAll()
                .test()
                .assertValue { categories ->
                    assertEquals("数が合っていません。", categories.size, 4)
                    true
                }
    }
}