package jp.hotdrop.moviememory.presentation.parts

import androidx.databinding.BindingAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage
import jp.hotdrop.moviememory.R

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, url: String?) {
    when {
        url.isNullOrEmpty() -> {
            GlideApp.with(view.context).clear(view)
            GlideApp.with(view.context)
                    .load(R.drawable.image_default)
                    .into(view)
        }
        else -> {
            val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url)
            GlideApp.with(view.context)
                    .load(storageRef)
                    .placeholder(R.drawable.image_default)
                    .into(view)
        }
    }
}

@BindingAdapter("webLinkColor")
fun setWebLinkColor(view: TextView, url: String?) {
    val textColor = if (url.isNullOrEmpty() || !url.startsWith("http")) {
        ContextCompat.getColor(view.context, R.color.lightGray)
    } else {
        ContextCompat.getColor(view.context, R.color.blue)
    }
    view.setTextColor(textColor)
}