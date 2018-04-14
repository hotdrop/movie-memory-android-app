package jp.hotdrop.moviememory.data

import jp.hotdrop.moviememory.data.remote.MockHttpClient
import jp.hotdrop.moviememory.data.remote.MockServer
import jp.hotdrop.moviememory.data.repository.MovieDataRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * sdk=23にしない場合はNetworkSecurityPoicyを自前で設定する必要がある。
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23], manifest = Config.NONE)
class MovieRepositoryTest {

    @Test
    fun loadNowPlayingMoviesTest() {
        val mockServer = MockServer()

        // getUrlを実行するとHttpUrl.Builderが走ってしまうためその前にMockServerを起動する
        mockServer.start()
        val repository = MovieDataRepository(MockHttpClient(mockServer.getUrl()).movieApi())

        // indexもoffsetも無視したテスト
        repository.loadNowPlayingMovies(1, 0).test().run {
            assertNoErrors()
            val response = values().first()
            println(" movie title ${response[0].title}")
            assert(response[0].title == "映画その1")
            assert(response[1].title == "映画その2")
            assertComplete()
        }
        mockServer.stop()
    }
}