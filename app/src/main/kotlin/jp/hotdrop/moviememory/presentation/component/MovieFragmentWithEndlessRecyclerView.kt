package jp.hotdrop.moviememory.presentation.component

import androidx.recyclerview.widget.RecyclerView
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.presentation.BaseFragment
import timber.log.Timber

/**
 * RecyclerViewのEndlessScrollとSwipeRefreshはTabに追加するFragment全部で共通動作なため共通のFragmentを設けた。
 */
abstract class MovieFragmentWithEndlessRecyclerView: BaseFragment() {

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    protected fun setupRecyclerView(recyclerView: androidx.recyclerview.widget.RecyclerView, runLoadMore: (page: Int, totalItemsCount: Int) -> Unit) {
        val gridLayoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, 3)
        recyclerView.layoutManager = gridLayoutManager
        scrollListener = (object: EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: androidx.recyclerview.widget.RecyclerView?) {
                runLoadMore(page, totalItemsCount)
            }
        })
        recyclerView.addOnScrollListener(scrollListener)
    }

    protected fun setupSwipeRefresh(swipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout, runRefresh: () -> Unit) {
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

        private var layoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager

        constructor(linearLayoutManager: androidx.recyclerview.widget.LinearLayoutManager) {
            layoutManager = linearLayoutManager
        }

        constructor(gridLayoutManager: androidx.recyclerview.widget.GridLayoutManager) {
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
                is androidx.recyclerview.widget.LinearLayoutManager -> { (layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastVisibleItemPosition() }
                is androidx.recyclerview.widget.GridLayoutManager -> { (layoutManager as androidx.recyclerview.widget.GridLayoutManager).findLastVisibleItemPosition() }
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

        abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: androidx.recyclerview.widget.RecyclerView?)
    }
}