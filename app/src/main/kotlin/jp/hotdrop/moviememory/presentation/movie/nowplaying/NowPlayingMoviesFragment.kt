package jp.hotdrop.moviememory.presentation.movie.nowplaying

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentNowPlayingMoviesBinding
import jp.hotdrop.moviememory.databinding.ItemMovieBinding
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.presentation.BaseFragment
import jp.hotdrop.moviememory.presentation.parts.BindingHolder
import jp.hotdrop.moviememory.presentation.parts.RecyclerViewAdapter
import javax.inject.Inject

class NowPlayingMoviesFragment: BaseFragment() {

    private lateinit var binding: FragmentNowPlayingMoviesBinding
    private lateinit var adapter: NowPlayingMoviesAdapter
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: NowPlayingMoviesViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(NowPlayingMoviesViewModel::class.java)
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

        // 通常通りに取得して、データが空ならリフレッシュする
        viewModel.movies.observe(this, Observer { movies ->
            movies?.let {
                if (movies.isEmpty()) {
                    viewModel.onRefreshWithRetry()
                    // これ当たり前だがリトライする前にでるので・・なんとかする
                    Toast.makeText(this@NowPlayingMoviesFragment.context, "データを取得できませんでした。", Toast.LENGTH_SHORT).show()
                } else {
                    binding.nowplayingProgress.visibility = View.GONE
                    // TODO 無条件にaddAllしているが、これは更新しないとダメ
                    // TODO 上スワイプは全removeして良い。下スワイプはそのアイテムだけInsertNotifyする
                    adapter.addAll(movies)
                }
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun setupRecyclerView() {
        binding.nowplayingMoviesRecyclerView.layoutManager = GridLayoutManager(activity, 3)
        adapter = NowPlayingMoviesAdapter()
        binding.nowplayingMoviesRecyclerView.adapter = adapter
    }

    private fun setupSwipeRefresh() {
        binding.nowPlayingSwipeRefresh.apply {
            setColorSchemeResources(R.color.colorAccent)
            setOnRefreshListener ({
                viewModel.onRefresh()
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
     * とりあえず個々のitemはViewModelを使わずにやってみる
     */
    inner class NowPlayingMoviesAdapter: RecyclerViewAdapter<Movie, BindingHolder<ItemMovieBinding>>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemMovieBinding> =
                BindingHolder(parent, R.layout.item_movie)

        override fun onBindViewHolder(holder: BindingHolder<ItemMovieBinding>, position: Int) {
            val binding = holder.binding
            binding.movie = getItem(position)

            // TODO onClickListenerとか
        }
    }
}