package jp.hotdrop.moviememory

import com.facebook.stetho.Stetho
import timber.log.Timber

class DebugMovieMemoryApp: MovieMemoryApp() {

    override fun onCreate() {
        super.onCreate()

        initTimber()
        initStetho()
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initStetho() {
        Stetho.initializeWithDefaults(this)
    }
}