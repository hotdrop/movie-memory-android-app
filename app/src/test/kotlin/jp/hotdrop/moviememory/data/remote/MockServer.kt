package jp.hotdrop.moviememory.data.remote

import okhttp3.HttpUrl
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * OkHttpのテスト用モックサーバー
 */
class MockServer {

    private val server = MockWebServer()

    private enum class UrlType {
        NowPlaying,
        ComingSoon
    }
    private val urlPatterns = mapOf(
            Pair("/movies/now-playing\\?index=\\d+\\&offset=\\d+", UrlType.NowPlaying),
            Pair("/movies/coming-soon\\?index=\\d+\\&offset=\\d+", UrlType.ComingSoon)
    )

    fun getUrl(): HttpUrl = server.url("/")

    fun start() {
        server.setDispatcher(dispatcher)
        server.start()
    }

    fun stop() {
        server.shutdown()
    }

    private val dispatcher = (object : Dispatcher() {
        override fun dispatch(request: RecordedRequest?): MockResponse {
            if (request == null || request.path == null) {
                return MockResponse().setResponseCode(400)
            }

            return when (urlType(request.path)) {
                UrlType.NowPlaying -> MockResponse().setBody(readJson("movies_now_playing.json")).setResponseCode(200)
                UrlType.ComingSoon -> MockResponse().setBody(readJson("movies_coming_soon.json")).setResponseCode(200)
            }
        }
    })

    private fun urlType(url: String): UrlType =
            urlPatterns.filter { it.key.toRegex().matches(url) }
                .map { it.value }
                .first()

    private fun readJson(jsonFileName: String): String {
        val sb = StringBuilder()
        val inputStream = javaClass.classLoader.getResourceAsStream(jsonFileName)
        BufferedReader(InputStreamReader(inputStream)).use {
            it.lines().forEach { sb.append(it) }
        }
        return sb.toString()
    }
}