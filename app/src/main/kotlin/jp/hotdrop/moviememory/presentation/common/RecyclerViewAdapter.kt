package jp.hotdrop.moviememory.presentation.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

abstract class RecyclerViewAdapter<T, VH: RecyclerView.ViewHolder>: RecyclerView.Adapter<VH>() {

    private val list: MutableList<T> = mutableListOf()

    override fun getItemCount() = list.size

    fun getAll(): List<T> = list

    fun getItem(index: Int) = list[index]

    fun getItemPosition(item: T): Int? {
        val index = list.indexOf(item)
        return if (index == -1) {
            null
        } else {
            index
        }
    }

    fun add(item: T) {
        list.add(item)
        getItemPosition(item)?.let {
            notifyItemInserted(it)
        }
    }

    fun addAll(items: List<T>) {
        val startIdx = if (list.size > 0) {
            list.size - 1
        } else {
            0
        }

        list.addAll(items)
        val itemCount = items.size

        Timber.d("Adapter addAll. 開始位置 $startIdx 更新数 $itemCount 総件数: ${list.size} ")
        notifyItemRangeChanged(startIdx, itemCount)
    }

    fun refresh(items: List<T>) {
        Timber.d("Adapter refresh.")
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    fun update(oldItem: T, newItem: T) {
        list.forEachIndexed { i, element ->
            if (element == oldItem) {
                list[i] = newItem
                notifyItemChanged(i)
                return@forEachIndexed
            }
        }
    }

    fun replace(index: Int, item: T) {
        list.forEachIndexed { i, _ ->
            if(i == index) {
                list[index] = item
                notifyItemChanged(i)
                return@forEachIndexed
            }
        }
    }

    fun remove(item: T) {
        getItemPosition(item)?.let {
            list.remove(item)
            notifyItemRemoved(it)
        }
    }

    /**
     * ViewAdapterのBindingHolder
     */
    class BindingHolder<out T: ViewDataBinding>(
            parent: ViewGroup,
            @LayoutRes layoutResId: Int
    ): RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)) {
        val binding: T? = DataBindingUtil.bind(itemView)
    }
}
