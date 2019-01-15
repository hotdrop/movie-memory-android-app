package jp.hotdrop.moviememory.di.component

import android.app.Activity

interface DaggerComponentProvider {
    val component: AppComponent
}

val Activity.component get() = (application as DaggerComponentProvider).component.plus()