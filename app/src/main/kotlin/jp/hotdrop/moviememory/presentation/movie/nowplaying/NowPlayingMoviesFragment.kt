package jp.hotdrop.moviememory.presentation.movie.nowplaying

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentNowPlayingMoviesBinding
import jp.hotdrop.moviememory.databinding.ItemMovieBinding
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.presentation.BaseFragment
import jp.hotdrop.moviememory.presentation.NavigationController
import jp.hotdrop.moviememory.presentation.parts.BindingHolder
import jp.hotdrop.moviememory.presentation.parts.EndlessRecyclerViewScrollListener
import jp.hotdrop.moviememory.presentation.parts.RecyclerViewAdapter
import timber.log.Timber
import javax.inject.Inject

class NowPlayingMoviesFragment: BaseFragment() {

    @Inject
    lateinit var navigationController: NavigationController

    private lateinit var binding: FragmentNowPlayingMoviesBinding
    private lateinit var adapter: NowPlayingMoviesAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: NowPlayingMoviesViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(NowPlayingMoviesViewModel::class.java)
    }

    // TODO 時代を逆行して状態を持ってしまった。。もっといいやり方がないか模索する・・
    private var nowObserveState = ObserveState.Normal
    private enum class ObserveState {
        Normal,  // 一覧に、取得したアイテムを追加していく
        Refresh, // 一覧のアイテムを全部クリアして、取得したアイテムをセットする
        OneStop  // 何もしない。onResumeでLiveDataがActiveになってしまうのでこれで止める
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        getComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNowPlayingMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()
        setupSwipeRefresh()

        viewModel.movies.observe(this, Observer {
            it?.let { movies ->
                binding.nowplayingProgress.visibility = View.GONE
                when (nowObserveState) {
                    ObserveState.Normal -> adapter.addAll(movies)
                    ObserveState.Refresh -> adapter.refresh(movies)
                    ObserveState.OneStop -> Timber.i("画面更新しない")
                }
                nowObserveState = ObserveState.Normal
            }
        })
        lifecycle.addObserver(viewModel)
    }

    override fun onStop() {
        super.onStop()
        // このステータスはsaveInstanceはしない
        // 理由はFragment自体がkillされたら再度LiveDataがアクティブにならないとデータ取ってこれないので。
        nowObserveState = ObserveState.OneStop
    }

    private fun setupRecyclerView() {
        binding.nowplayingMoviesRecyclerView.let {
            val gridLayoutManager = GridLayoutManager(activity, 3)
            it.layoutManager = gridLayoutManager
            scrollListener = (object: EndlessRecyclerViewScrollListener(gridLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    viewModel.onLoad(page) {
                        Toast.makeText(this@NowPlayingMoviesFragment.context, "データを取得できませんでした。", Toast.LENGTH_SHORT).show()
                    }
                }
            })
            it.addOnScrollListener(scrollListener)
            adapter = NowPlayingMoviesAdapter()
            it.adapter = adapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.nowPlayingSwipeRefresh.apply {
            setColorSchemeResources(R.color.colorAccent)
            setOnRefreshListener ({
                Timber.i("start Refresh")
                scrollListener.reset()
                nowObserveState = ObserveState.Refresh
                viewModel.onRefresh {
                    Toast.makeText(this.context, "データを取得できませんでした。", Toast.LENGTH_SHORT).show()
                }
                if (binding.nowPlayingSwipeRefresh.isRefreshing) {
                    binding.nowPlayingSwipeRefresh.isRefreshing = false
                }
            })
        }
    }

    companion object {
        fun newInstance() = NowPlayingMoviesFragment()
    }

    /**
     * Adapter
     */
    inner class NowPlayingMoviesAdapter: RecyclerViewAdapter<Movie, BindingHolder<ItemMovieBinding>>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemMovieBinding> =
                BindingHolder(parent, R.layout.item_movie)

        override fun onBindViewHolder(holder: BindingHolder<ItemMovieBinding>, position: Int) {
            val binding = holder.binding
            binding?.let {
                val movie = getItem(position)
                it.movie = movie
                it.imageView.setOnClickListener { _ ->
                    navigationController.navigateToMovieDetail(movie.id)
                }
            }
        }
    }
}