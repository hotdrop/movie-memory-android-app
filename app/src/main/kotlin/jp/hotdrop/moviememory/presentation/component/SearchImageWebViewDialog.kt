package jp.hotdrop.moviememory.presentation.component

import android.content.Context
import android.view.LayoutInflater
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import dagger.Reusable
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.DialogSearchImageWebViewBinding
import javax.inject.Inject

@Reusable
class SearchImageWebViewDialog @Inject constructor() {

    private lateinit var binding: DialogSearchImageWebViewBinding
    private lateinit var dialog: AlertDialog
    private lateinit var onClickImageListener: ((imageUrl: String) -> Unit)

    fun show(context: Context, onClickImageListener: (imageUrl: String) -> Unit) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_search_image_web_view, null, false)
        this.onClickImageListener = {
            onClickImageListener(it)
            dialog.dismiss()
        }

        binding.searchImageWebView.let {
            it.webViewClient = SearchImageWebViewClient()
            it.settings.cacheMode = WebSettings.LOAD_NO_CACHE
            it.loadUrl(context.getString(R.string.image_search_dialog_url))
        }

        dialog = AlertDialog.Builder(context)
                .setView(binding.root)
                .setCancelable(true)
                .create()

        binding.closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private inner class SearchImageWebViewClient: WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding.progress.isGone = true
            binding.searchImageWebView.isVisible = true
        }

        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            binding.progress.isGone = true
            binding.searchImageWebView.isVisible = true
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            request?.let {
                val url = it.url.toString()
                if (url.endsWith(".jpg") || url.endsWith(".png")) {
                    onClickImageListener(url)
                    return false
                }
            }
            return super.shouldOverrideUrlLoading(view, request)
        }
    }
}