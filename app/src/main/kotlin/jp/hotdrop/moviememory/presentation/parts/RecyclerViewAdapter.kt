package jp.hotdrop.moviememory.presentation.parts

import android.support.v7.widget.RecyclerView

abstract class RecyclerViewAdapter<T, VH: RecyclerView.ViewHolder>: RecyclerView.Adapter<VH>() {

    private val list: MutableList<T> = mutableListOf()

    override fun getItemCount() = list.size

    fun addAll(items: List<T>) {
        val startIdx = if (list.size > 0) list.size - 1 else 0
        list.addAll(items)
        val endIdx = list.size - 1
        notifyItemRangeChanged(startIdx, endIdx)
    }

    fun clear() {
        list.clear()
    }

    fun getItem(index: Int) = list[index]
}