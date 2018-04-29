package jp.hotdrop.moviememory.presentation.movie.nowplaying

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        // RecyclerViewの初期化
        adapter = NowPlayingMoviesAdapter()
        binding.nowplayingMoviesRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.movies.observe(this, Observer { movies ->
            movies?.let {
                binding.nowplayingProgress.visibility = View.GONE
                adapter.addAll(movies)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.onRefresh()
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