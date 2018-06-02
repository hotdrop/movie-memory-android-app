package jp.hotdrop.moviememory.presentation.parts

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class EndlessRecyclerViewScrollListener: RecyclerView.OnScrollListener {

    private val visibleThreshold = 5
    private var currentPage = 0
    private var previousTotalItemCount = 0
    private var loading = true
    private val startPageIndex = 0

    private var layoutManager: RecyclerView.LayoutManager

    constructor(linearLayoutManager: LinearLayoutManager) {
        layoutManager = linearLayoutManager
    }

    constructor(gridLayoutManager: GridLayoutManager) {
        layoutManager = gridLayoutManager
    }

    fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int =
        lastVisibleItemPositions.max() ?: lastVisibleItemPositions[0]

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {

        val totalItemCount = layoutManager.itemCount
        if (totalItemCount < previousTotalItemCount) {
            currentPage = startPageIndex
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }

        if (stillLoading()) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        val lastVisibleItemPosition = when (layoutManager) {
            is LinearLayoutManager -> { (layoutManager as LinearLayoutManager).findLastVisibleItemPosition() }
            is GridLayoutManager -> { (layoutManager as GridLayoutManager).findLastVisibleItemPosition() }
            else -> throw IllegalStateException("layoutManager is incorrect! ${layoutManager.javaClass}")
        }

        if (notLoadingAndNeedToReloadMoreData(lastVisibleItemPosition)) {
            currentPage++
            onLoadMore(currentPage, totalItemCount, recyclerView)
            loading = true
        }
    }

    private fun stillLoading(): Boolean =
            loading && (layoutManager.itemCount > previousTotalItemCount)

    private fun notLoadingAndNeedToReloadMoreData(lastVisibleItemPosition: Int): Boolean =
            !loading && ((lastVisibleItemPosition + visibleThreshold) > layoutManager.itemCount)

    fun reset() {
        currentPage = startPageIndex
        previousTotalItemCount = 0
        loading = true
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?)
 }