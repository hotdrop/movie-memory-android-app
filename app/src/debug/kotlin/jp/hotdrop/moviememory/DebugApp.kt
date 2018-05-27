package jp.hotdrop.moviememory

import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

class DebugApp: App() {

    override fun onCreate() {
        super.onCreate()

        initLeakCanary()
        initTimber()
        initStetho()
    }

    private fun initLeakCanary() {
        LeakCanary.install(this)
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initStetho() {
        Stetho.initializeWithDefaults(this)
    }
}