package jp.hotdrop.moviememory.data

import jp.hotdrop.moviememory.data.remote.MockHttpClient
import jp.hotdrop.moviememory.data.remote.MockServer
import jp.hotdrop.moviememory.data.repository.MovieDataRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * sdk=23にしない場合はNetworkSecurityPolicyを自前で実装する必要がある。
 * それが面倒だったのでsdk=23で済ませる。
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23], manifest = Config.NONE)
class MovieRepositoryTest {

    @Test
    fun loadNowPlayingMoviesTest() {
        testOnMockServer { repository ->
            repository.loadNowPlayingMovies(1, 0).test().run {
                assertNoErrors()
                assertValueAt(0, { movies ->
                    movies[0].title == "映画その1" && movies[1].title == "映画その2"
                })
                assertComplete()
            }
        }
    }

    @Test
    fun loadComingSoonMoviesTest() {
        testOnMockServer { repository ->
            repository.loadComingSoonMovies(1, 0).test().run {
                assertNoErrors()
                assertValueAt(0, { movies ->
                    movies[0].title == "近日公開その1" && movies[1].title == "近日公開その2"
                })
                assertComplete()
            }
        }
    }

    private fun testOnMockServer(test: (repo: MovieDataRepository) -> Unit) {
        MockServer().run {
            start()
            val repository = MovieDataRepository(MockHttpClient(this.getUrl()).movieApi())
            try {
                test(repository)
            } finally {
                stop()
            }
        }
    }
}