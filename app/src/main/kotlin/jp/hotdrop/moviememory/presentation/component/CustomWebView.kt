package jp.hotdrop.moviememory.presentation.component

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.webkit.WebView

class CustomWebView: WebView {

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle)

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (this.canGoBack()) {
                        this.goBack()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onCheckIsTextEditor(): Boolean {
        return true
    }
}