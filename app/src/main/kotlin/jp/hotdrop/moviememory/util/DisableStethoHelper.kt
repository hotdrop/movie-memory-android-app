package jp.hotdrop.moviememory.util

import android.content.Context

/**
 * Stethoを無効にする場合、このクラスをBuildConfigで実装する。
 * リリース版は必ずこっちを指定すること。
 */
class DisableStethoHelper: StethoHelper {
    override fun init(context: Context) {
        /* 何もしない。 no operation */
    }
}