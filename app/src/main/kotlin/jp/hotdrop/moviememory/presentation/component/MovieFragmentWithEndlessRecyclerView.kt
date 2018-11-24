package jp.hotdrop.moviememory.presentation.component

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.presentation.BaseFragment
import timber.log.Timber

/**
 * RecyclerViewのEndlessScrollとSwipeRefreshはTabに追加するFragment全部で共通動作なため共通のFragmentを設けた。
 */
abstract class MovieFragmentWithEndlessRecyclerView: BaseFragment() {

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    protected fun setupRecyclerView(recyclerView: RecyclerView, runLoadMore: (page: Int, totalItemsCount: Int) -> Unit) {
        val gridLayoutManager = GridLayoutManager(activity, 2)
        recyclerView.layoutManager = gridLayoutManager
        scrollListener = (object: EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                runLoadMore(page, totalItemsCount)
            }
        })
        recyclerView.addOnScrollListener(scrollListener)
    }

    protected fun setupSwipeRefresh(swipeRefreshLayout: SwipeRefreshLayout, runRefresh: () -> Unit) {
        swipeRefreshLayout.let {
            it.setColorSchemeResources(R.color.colorAccent)
            it.setOnRefreshListener {
                Timber.i("start Refresh")
                scrollListener.reset()
                runRefresh()
                if (swipeRefreshLayout.isRefreshing) {
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    /**
     * エンドレススクロールのリスナークラス
     */
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

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

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
                is LinearLayoutManager -> {
                    (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                }
                is GridLayoutManager -> {
                    (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                }
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
}