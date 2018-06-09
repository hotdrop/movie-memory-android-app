package jp.hotdrop.moviememory.presentation.movie.nowplaying

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentNowPlayingMoviesBinding
import jp.hotdrop.moviememory.databinding.ItemMovieBinding
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.presentation.NavigationController
import jp.hotdrop.moviememory.presentation.component.MovieFragmentWithEndlessRecyclerView
import jp.hotdrop.moviememory.presentation.parts.BindingHolder
import jp.hotdrop.moviememory.presentation.parts.RecyclerViewAdapter
import timber.log.Timber
import javax.inject.Inject

class NowPlayingMoviesFragment: MovieFragmentWithEndlessRecyclerView() {

    @Inject
    lateinit var navigationController: NavigationController
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: NowPlayingMoviesViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(NowPlayingMoviesViewModel::class.java)
    }

    private lateinit var binding: FragmentNowPlayingMoviesBinding
    private lateinit var adapter: NowPlayingMoviesAdapter

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

        initView()

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

    private fun initView() {
        super.setupRecyclerView(binding.nowplayingMoviesRecyclerView) { page: Int, _: Int ->
            viewModel.onLoad(page) {
                Toast.makeText(this@NowPlayingMoviesFragment.context, "データを取得できませんでした。", Toast.LENGTH_SHORT).show()
            }
        }
        adapter = NowPlayingMoviesAdapter()
        binding.nowplayingMoviesRecyclerView.adapter = adapter

        super.setupSwipeRefresh(binding.nowPlayingSwipeRefresh) {
            nowObserveState = ObserveState.Refresh
            viewModel.onRefresh {
                Toast.makeText(this.context, "データを取得できませんでした。", Toast.LENGTH_SHORT).show()
            }
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