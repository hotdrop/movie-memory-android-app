package jp.hotdrop.moviememory.presentation.parts

import android.support.v7.util.DiffUtil

private class RecyclerDiffCallback(
        private val oldList: List<RecyclerDiffable>,
        private val newList: List<RecyclerDiffable>
): DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].isItemTheSame(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].isContentsTheSame(newList[newItemPosition])
}

interface RecyclerDiffable {
    fun isItemTheSame(o: RecyclerDiffable): Boolean
    fun isContentsTheSame(o: RecyclerDiffable): Boolean
}

fun calculateDiff(
        oldList: List<RecyclerDiffable>,
        newList: List<RecyclerDiffable>,
        detectMoves: Boolean = false
): DiffUtil.DiffResult {
    return DiffUtil.calculateDiff(RecyclerDiffCallback(oldList, newList), detectMoves)
}