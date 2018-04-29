package jp.hotdrop.moviememory.presentation.parts

import android.support.v7.widget.RecyclerView

abstract class RecyclerViewAdapter<T, VH: RecyclerView.ViewHolder>: RecyclerView.Adapter<VH>() {

    private val list: MutableList<T> = mutableListOf()

    override fun getItemCount() = list.size

    fun addAll(items: List<T>) {
        list.addAll(items)
    }

    fun getItem(index: Int) = list[index]
}