package jp.hotdrop.moviememory.data

import com.nhaarman.mockito_kotlin.mock
import com.squareup.moshi.Moshi
import jp.hotdrop.moviememory.data.local.MovieDatabase
import jp.hotdrop.moviememory.data.remote.AppJsonAdapterFactory
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * sdk=23にしない場合はNetworkSecurityPolicyを自前で実装する必要がある。
 * それが面倒だったのでsdk=23で済ませる。
 *
 * TODO そもそもAPIをテストする必要があるのかかなり疑問。。
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23], manifest = Config.NONE)
class MovieRepositoryTest {

    private val movieDatabase: MovieDatabase = mock()
    private val moshi = Moshi.Builder().add(AppJsonAdapterFactory.INSTANCE).build()
//    private val listOfMovieEntityType = Types.newParameterizedType(List::class.java, MovieResult::class.java)


    // TODO テストは全部見直す
    fun preparedTest() {
    }

    @Test
    fun findMoviesTest() {
//        val movieEntities = createMoviesForReadJson("movies_now_playing.json")
//        MockServer().run {
//            start()
//            val repository = MovieRepository(MockHttpClient(this.getUrl()).movieApi(), movieDatabase)
//            repository.findNowPlayingMovies(1, 2)
//                    .test()
//                    .assertNoErrors()
//                    .assertComplete()
//            verify(movieDatabase).save(movieEntities)
//            stop()
//        }
    }

    @Test
    fun loadRecentMoviesTest() {
    }

//    private fun createMoviesForReadJson(jsonFileName: String): List<MovieEntity> {
//        val movieEntities = mutableListOf<MovieEntity>()
//        val jsonAdapter: JsonAdapter<List<MovieResult>> = moshi.adapter(listOfMovieEntityType)
//        val inputStream = javaClass.classLoader.getResourceAsStream(jsonFileName)
//        BufferedReader(InputStreamReader(inputStream)).use {
//            val sb = StringBuilder()
//            it.lines().forEach { sb.append(it) }
//            jsonAdapter.fromJson(sb.toString())?.map { it.toMovieEntity() }?.run {
//                movieEntities.addAll(this)
//            }
//        }
//        return movieEntities
//    }
}