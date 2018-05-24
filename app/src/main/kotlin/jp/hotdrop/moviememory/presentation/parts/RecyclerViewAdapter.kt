package jp.hotdrop.moviememory.presentation.parts

import android.support.v7.widget.RecyclerView
import timber.log.Timber
import kotlin.properties.Delegates

abstract class RecyclerViewAdapter<T: RecyclerDiffable, VH: RecyclerView.ViewHolder>: RecyclerView.Adapter<VH>() {

    private val list: MutableList<T> by Delegates.observable(mutableListOf()) { _, oldList, newList ->
        calculateDiff(oldList, newList).dispatchUpdatesTo(this)
    }

    override fun getItemCount() = list.size

    fun addAll(items: List<T>) {
        val startIdx = if (list.size > 0) list.size - 1 else 0
        list.addAll(items)
        val endIdx = list.size - 1

        Timber.d(" Adapter addAll. 開始位置 $startIdx 終了位置 $endIdx 総件数: ${list.size} ")
        // RangeChangeでIndexOutOfBoundsException
        notifyItemRangeChanged(startIdx, endIdx)
    }

    fun clear() {
        list.clear()
    }

    fun getItem(index: Int) = list[index]
}