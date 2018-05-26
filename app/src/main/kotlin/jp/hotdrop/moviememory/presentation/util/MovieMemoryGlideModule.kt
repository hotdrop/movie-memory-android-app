package jp.hotdrop.moviememory.presentation.util

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import jp.hotdrop.moviememory.App
import okhttp3.OkHttpClient
import java.io.InputStream
import javax.inject.Inject

@GlideModule
class MovieMemoryGlideModule: AppGlideModule() {

    // 本当はコンストラクタインジェクションにしたかったがアノテーションプロセッサにより
    // DIする前にこのクラスのImplクラスが生成されてしまいエラーになる。
    // なので仕方ないからフィールドインジェクションにする。
    @Inject
    lateinit var okHttpClient: OkHttpClient

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        (context.applicationContext as App).getComponent().plus(this)
        glide.registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(okHttpClient))
    }
}