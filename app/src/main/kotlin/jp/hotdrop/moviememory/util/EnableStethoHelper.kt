package jp.hotdrop.moviememory.util

import android.content.Context
import com.facebook.stetho.Stetho

/**
 * Stethoを有効にする場合、このクラスをBuildConfigで実装する。
 * 基本はDebugなど開発時にしか使わないBuildTypeやFlavorで指定すること。
 */
class EnableStethoHelper: StethoHelper {
    override fun init(context: Context) {
        Stetho.initializeWithDefaults(context)
    }
}