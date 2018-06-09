package jp.hotdrop.moviememory.presentation.parts

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.google.firebase.storage.FirebaseStorage
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.presentation.util.GlideApp

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, url: String?) {
    if (url.isNullOrEmpty()) {
        return
    }
    val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url!!)
    GlideApp.with(view.context)
            .load(storageRef)
            .placeholder(R.drawable.image_default)
            .into(view)
}