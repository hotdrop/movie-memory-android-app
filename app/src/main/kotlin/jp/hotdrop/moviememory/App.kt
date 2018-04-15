package jp.hotdrop.moviememory

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import jp.hotdrop.moviememory.di.AppComponent
import jp.hotdrop.moviememory.di.AppModule
import jp.hotdrop.moviememory.di.DaggerAppComponent

class App: Application() {

    private lateinit var mainComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        mainComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    fun getComponent(): AppComponent = mainComponent
}