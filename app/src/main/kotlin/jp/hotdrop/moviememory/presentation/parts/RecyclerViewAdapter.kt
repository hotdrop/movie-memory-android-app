package jp.hotdrop.moviememory.presentation.parts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import timber.log.Timber
import kotlin.properties.Delegates

abstract class RecyclerViewAdapter<T: RecyclerDiffable, VH: androidx.recyclerview.widget.RecyclerView.ViewHolder>: androidx.recyclerview.widget.RecyclerView.Adapter<VH>() {

    private val list: MutableList<T> by Delegates.observable(mutableListOf()) { _, oldList, newList ->
        calculateDiff(oldList, newList).dispatchUpdatesTo(this)
    }

    override fun getItemCount() = list.size

    fun addAll(items: List<T>) {
        val startIdx = if (list.size > 0) list.size - 1 else 0

        list.addAll(items)
        val endIdx = list.size - 1

        Timber.d("Adapter addAll. 開始位置 $startIdx 終了位置 $endIdx 総件数: ${list.size} ")
        notifyItemRangeChanged(startIdx, endIdx)
    }

    fun refresh(items: List<T>) {
        Timber.d("Adapter refresh.")
        clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        list.clear()
    }

    fun getItem(index: Int) = list[index]

    /**
     * ViewAdapterのBindingHolder
     */
    class BindingHolder<out T: ViewDataBinding>(
            parent: ViewGroup,
            @LayoutRes layoutResId: Int
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)) {
        val binding: T? = DataBindingUtil.bind(itemView)
    }
}
