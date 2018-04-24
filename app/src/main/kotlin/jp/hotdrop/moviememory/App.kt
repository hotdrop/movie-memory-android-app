package jp.hotdrop.moviememory

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import jp.hotdrop.moviememory.di.component.AppComponent
import jp.hotdrop.moviememory.di.component.DaggerAppComponent
import jp.hotdrop.moviememory.di.module.AppModule

class App: Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    fun getComponent(): AppComponent = appComponent
}