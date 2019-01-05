package jp.hotdrop.moviememory.presentation.parts

import androidx.databinding.BindingAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import jp.hotdrop.moviememory.R
import timber.log.Timber

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
            val imageUrl = if (url.endsWith("jpg") || url.endsWith("png")) {
                url
            } else {
                Timber.d("ImageURLがおかしい. url=$url")
                ""
            }
            GlideApp.with(view.context)
                    .load(imageUrl)
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

@BindingAdapter("imageFavoritesStar")
fun setImageFavoriteStar(view: LottieAnimationView, count: Int) {
    if (count > 0) {
        view.playAnimation()
    } else {
        view.progress = 0f
    }
}