package jp.hotdrop.moviememory

import com.facebook.stetho.Stetho
import timber.log.Timber

class DebugMovieMemoryApp: MovieMemoryApp() {

    override fun onCreate() {
        super.onCreate()

        initStetho()
    }

    private fun initStetho() {
        Stetho.initializeWithDefaults(this)
    }
}