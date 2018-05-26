package jp.hotdrop.moviememory

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.leakcanary.LeakCanary
import jp.hotdrop.moviememory.di.component.AppComponent
import jp.hotdrop.moviememory.di.component.DaggerAppComponent
import jp.hotdrop.moviememory.di.module.AppModule
import timber.log.Timber

class App: Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        if (!canInitApp()) {
            return
        }

        initLeakCanary()
        initTreeTen()
        initTimber()
        initStetho()

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    fun getComponent(): AppComponent = appComponent

    private fun canInitApp(): Boolean =
            !LeakCanary.isInAnalyzerProcess(this)

    private fun initLeakCanary() {
        LeakCanary.install(this)
    }
    private fun initTreeTen() {
        AndroidThreeTen.init(this)
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initStetho() {
        BuildConfig.STETHO.init(this)
    }
}