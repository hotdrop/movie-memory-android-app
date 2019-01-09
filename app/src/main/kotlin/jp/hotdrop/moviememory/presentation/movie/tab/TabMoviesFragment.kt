package jp.hotdrop.moviememory.presentation.movie.tab

import android.app.Activity
import android.app.ActivityOptions
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentMoviesTabBinding
import jp.hotdrop.moviememory.databinding.ItemMovieBinding
import jp.hotdrop.moviememory.di.component.component
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.model.MovieCondition
import jp.hotdrop.moviememory.presentation.component.MovieFragmentWithEndlessRecyclerView
import jp.hotdrop.moviememory.presentation.movie.detail.MovieDetailActivity
import jp.hotdrop.moviememory.presentation.common.RecyclerViewAdapter
import timber.log.Timber

class TabMoviesFragment: MovieFragmentWithEndlessRecyclerView() {

    private lateinit var binding: FragmentMoviesTabBinding
    private lateinit var parentActivity: Activity

    private var adapter: TabMoviesAdapter? = null

    private val viewModel: TabMoviesViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(TabMoviesViewModel::class.java)
    }

    private var loadMode = LoadMode.Add
    private enum class LoadMode {
        Add,     // 取得したアイテムを一覧に追加していく。add済みのデータには触らない
        Refresh, // 一覧のアイテムを全部クリアし、取得したアイテムをセットする
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        activity?.let {
            it.component.fragment().inject(this)
            parentActivity = it
        } ?: kotlin.run {
            Timber.d("onAttachが呼ばれましたがgetActivityがnullだったので終了します")
            onDestroy()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMoviesTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.condition = arguments?.getSerializable(MovieCondition.ARGUMENT_TAG) as MovieCondition

        initView()
        observe()
    }

    private fun initView() {
        super.setupRecyclerView(binding.moviesRecyclerView) { page, _ ->
            viewModel.onLoad(page)
        }
        adapter = TabMoviesAdapter()
        binding.moviesRecyclerView.adapter = adapter

        super.setupSwipeRefresh(binding.swipeRefresh) {
            loadMode = LoadMode.Refresh
            viewModel.onRefresh()
        }
    }

    private fun observe() {
        viewModel.movies.observe(this, Observer {
            it?.let { movies ->
                binding.progress.visibility = View.GONE
                when (loadMode) {
                    LoadMode.Add -> adapter?.addAll(movies)
                    LoadMode.Refresh -> adapter?.refresh(movies)
                }
                loadMode = LoadMode.Add
            }
        })
        viewModel.refreshMovie.observe(this, Observer {
            it?.let { movie ->
                adapter?.refresh(movie)
                viewModel.clear()
            }
        })
        viewModel.error.observe(this, Observer {
            it?.let {
                val message = getString(R.string.message_failure_load_data)
                Snackbar.make(binding.moviesArea, message, Snackbar.LENGTH_LONG).show()
                viewModel.clear()
            }
        })
        lifecycle.addObserver(viewModel)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }

        if (requestCode == REQUEST_CODE_TO_DETAIL) {
            val refreshMovieId = data.getLongExtra(MovieDetailActivity.EXTRA_MOVIE_TAG, -1)
            viewModel.onRefreshMovie(refreshMovieId)
        }
    }

    /**
     * アダプター
     */
    inner class TabMoviesAdapter: RecyclerViewAdapter<Movie, RecyclerViewAdapter.BindingHolder<ItemMovieBinding>>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemMovieBinding> =
                BindingHolder(parent, R.layout.item_movie)

        override fun onBindViewHolder(holder: BindingHolder<ItemMovieBinding>, position: Int) {
            val binding = holder.binding
            binding?.let {
                val movie = getItem(position)
                it.movie = movie
                onTapNavigationDetail(binding, movie)
            }
        }

        private fun onTapNavigationDetail(binding: ItemMovieBinding ,movie: Movie) {
            binding.movieLayout.setOnClickListener {
                transitionWithSharedElements(binding, movie)
            }
            binding.imageView.setOnClickListener {
                transitionWithSharedElements(binding, movie)
            }
        }

        private fun transitionWithSharedElements(binding: ItemMovieBinding ,movie: Movie) {
            // アホっぽいけどtransitionするため1〜5まで用意する
            val options = ActivityOptions.makeSceneTransitionAnimation(
                    parentActivity,
                    Pair.create(binding.favoritesStar as View, parentActivity.getString(R.string.transition_favorite_star1)),
                    Pair.create(binding.favoritesStar as View, parentActivity.getString(R.string.transition_favorite_star2)),
                    Pair.create(binding.favoritesStar as View, parentActivity.getString(R.string.transition_favorite_star3)),
                    Pair.create(binding.favoritesStar as View, parentActivity.getString(R.string.transition_favorite_star4)),
                    Pair.create(binding.favoritesStar as View, parentActivity.getString(R.string.transition_favorite_star5)),
                    Pair.create(binding.imageView as View, parentActivity.getString(R.string.transition_movie_image))
            )
            MovieDetailActivity.startForResult(this@TabMoviesFragment, movie.id, REQUEST_CODE_TO_DETAIL, options)
        }

        fun refresh(movie: Movie) {
            adapter?.let { adapter ->
                adapter.getItemPosition(movie)?.let { index ->
                    val oldMovie = adapter.getItem(index)
                    val newMovie = Movie.copyAll(oldMovie)
                    adapter.replace(index, newMovie)
                    notifyItemChanged(index)
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_TO_DETAIL = 1000
        fun newInstance() = TabMoviesFragment()
    }
}