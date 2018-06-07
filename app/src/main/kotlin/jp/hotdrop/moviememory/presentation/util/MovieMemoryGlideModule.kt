package jp.hotdrop.moviememory.presentation.util

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.StorageReference
import jp.hotdrop.moviememory.App
import java.io.InputStream

@GlideModule
class MovieMemoryGlideModule: AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        (context.applicationContext as App).getComponent().plus(this)
        glide.registry.replace(StorageReference::class.java, InputStream::class.java, FirebaseImageLoader.Factory())
    }
}