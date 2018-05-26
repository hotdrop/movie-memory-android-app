package jp.hotdrop.moviememory.presentation.util

import android.databinding.BindingAdapter
import android.widget.ImageView
import jp.hotdrop.moviememory.R

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, url: String) {
    if (url.isEmpty()) {
        return
    }
    GlideApp.with(view.context)
            .load(url)
            .placeholder(R.drawable.image_default)
            .into(view)
}