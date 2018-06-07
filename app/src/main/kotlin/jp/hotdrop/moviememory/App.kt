package jp.hotdrop.moviememory

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import jp.hotdrop.moviememory.di.component.AppComponent
import jp.hotdrop.moviememory.di.component.DaggerAppComponent
import jp.hotdrop.moviememory.di.module.AppModule

open class App: Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        initTreeTenABP()

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    fun getComponent(): AppComponent = appComponent

    private fun initTreeTenABP() {
        AndroidThreeTen.init(this)
    }
}