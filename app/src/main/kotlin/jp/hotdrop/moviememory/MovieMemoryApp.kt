package jp.hotdrop.moviememory

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.jakewharton.threetenabp.AndroidThreeTen
import jp.hotdrop.moviememory.di.component.AppComponent
import jp.hotdrop.moviememory.di.component.DaggerAppComponent
import jp.hotdrop.moviememory.di.component.DaggerComponentProvider
import timber.log.Timber

open class MovieMemoryApp: Application(), DaggerComponentProvider {

    override val component: AppComponent by lazy {
        DaggerAppComponent.builder()
                .appContext(applicationContext)
                .build()
    }

    override fun onCreate() {
        super.onCreate()

        initTimber()
        initTreeTenABP()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(MovieMemoryDebugTree())
        } else {
            // Fabric.with(this, Crashlytics())
            Timber.plant(MovieMemoryReleaseTree())
        }
    }

    private fun initTreeTenABP() {
        AndroidThreeTen.init(this)
    }

    companion object {
        private class MovieMemoryDebugTree: Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String? {
                val className = super.createStackElementTag(element)?.split("$")?.get(0)
                return "[$className.kt: ${element.lineNumber} ${element.methodName} MovieMemoryTag]"
            }
        }

        private class MovieMemoryReleaseTree: Timber.Tree() {
            override fun isLoggable(tag: String?, priority: Int): Boolean {
                return priority > Log.INFO
            }

            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                when (priority) {
                    Log.ERROR -> {
                        // TODO FirebaseCrashlyticsを使うときにここ設定する
                        // Crashlytics.setString("tag", tag)
                        // Crashlytics.setString("message", message)
                        // Crashlytics.logException(t)
                    }
                }
            }
        }
    }
}