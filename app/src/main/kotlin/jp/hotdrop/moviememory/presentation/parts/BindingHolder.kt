package jp.hotdrop.moviememory.presentation.parts

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class BindingHolder<out T: ViewDataBinding>(
        parent: ViewGroup,
        @LayoutRes layoutResId: Int
): androidx.recyclerview.widget.RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)) {
    val binding: T? = DataBindingUtil.bind(itemView)
}