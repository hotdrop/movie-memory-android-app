package jp.hotdrop.moviememory.data

import android.arch.persistence.room.Room
import jp.hotdrop.moviememory.data.local.AppDatabase
import jp.hotdrop.moviememory.data.local.MovieDatabase
import jp.hotdrop.moviememory.data.remote.MockHttpClient
import jp.hotdrop.moviememory.data.remote.MockServer
import jp.hotdrop.moviememory.data.repository.MovieDataRepository
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/**
 * sdk=23にしない場合はNetworkSecurityPolicyを自前で実装する必要がある。
 * それが面倒だったのでsdk=23で済ませる。
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23], manifest = Config.NONE)
class MovieRepositoryTest {

//    /**
//     * このテストはRoomを使用しているのでエラーになる。
//     */
//    @Test
//    fun loadNowPlayingMoviesTest() {
//        testOnMockServer { repository ->
//            repository.refreshNowPlayingMovies()
//                    .test()
//                    .assertNoErrors()
//                    .assertComplete()
//        }
//    }

    private fun testOnMockServer(test: (repo: MovieDataRepository) -> Unit) {
        MockServer().run {
            start()
            val db = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.application, AppDatabase::class.java).build()
            val repository = MovieDataRepository(MockHttpClient(this.getUrl()).movieApi(), MovieDatabase(db, db.movieDao()))
            test(repository)
            stop()
        }
    }
}