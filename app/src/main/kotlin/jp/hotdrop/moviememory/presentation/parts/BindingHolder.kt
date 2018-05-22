package jp.hotdrop.moviememory.presentation.parts

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class BindingHolder<out T: ViewDataBinding>(
        parent: ViewGroup,
        @LayoutRes layoutResId: Int
): RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)) {
    val binding: T? = DataBindingUtil.bind(itemView)
}