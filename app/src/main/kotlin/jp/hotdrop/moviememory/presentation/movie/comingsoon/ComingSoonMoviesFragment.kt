package jp.hotdrop.moviememory.presentation.movie.comingsoon

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentComingSoonMoviesBinding
import jp.hotdrop.moviememory.databinding.ItemMovieBinding
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.presentation.MainActivity
import jp.hotdrop.moviememory.presentation.component.MovieFragmentWithEndlessRecyclerView
import jp.hotdrop.moviememory.presentation.movie.detail.MovieDetailActivity
import jp.hotdrop.moviememory.presentation.movie.nowplaying.NowPlayingMoviesFragment
import jp.hotdrop.moviememory.presentation.parts.RecyclerViewAdapter
import timber.log.Timber
import javax.inject.Inject

class ComingSoonMoviesFragment: MovieFragmentWithEndlessRecyclerView() {

    private lateinit var binding: FragmentComingSoonMoviesBinding
    private lateinit var adapter: ComingSoonMoviesAdapter
    private var activity: MainActivity? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: ComingSoonMoviesViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ComingSoonMoviesViewModel::class.java)
    }

    private var nowObserveState = ObserveState.Add
    private enum class ObserveState {
        Add,     // 取得したアイテムを一覧に追加していく
        Refresh, // 一覧のアイテムを全部クリアして、取得したアイテムをセットする
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        getComponent().inject(this)
        (getActivity() as? MainActivity)?.let {
            activity = it
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentComingSoonMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        observe()
    }

    private fun initView() {
        super.setupRecyclerView(binding.moviesRecyclerView) { page, _ ->
            viewModel.onLoad(page)
        }
        adapter = ComingSoonMoviesAdapter()
        binding.moviesRecyclerView.adapter = adapter

        super.setupSwipeRefresh(binding.swipeRefresh) {
            nowObserveState = ObserveState.Refresh
            viewModel.onRefresh()
        }
    }

    private fun observe() {
        viewModel.movies.observe(this, Observer {
            it?.let { movies ->
                binding.progress.visibility = View.GONE
                when (nowObserveState) {
                    ObserveState.Add -> adapter.addAll(movies)
                    ObserveState.Refresh -> adapter.refresh(movies)
                }
                nowObserveState = ObserveState.Add
            }
        })
        viewModel.refreshMovie.observe(this, Observer {
            it?.let { movie ->
                adapter.refresh(movie)
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
            val refreshMovieId = data.getIntExtra(MovieDetailActivity.EXTRA_MOVIE_TAG, -1)
            viewModel.onRefreshMovie(refreshMovieId)
        }
    }

    /**
     * アダプター
     */
    inner class ComingSoonMoviesAdapter: RecyclerViewAdapter<Movie, RecyclerViewAdapter.BindingHolder<ItemMovieBinding>>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemMovieBinding> =
                BindingHolder(parent, R.layout.item_movie)

        override fun onBindViewHolder(holder: BindingHolder<ItemMovieBinding>, position: Int) {
            val binding = holder.binding
            binding?.let {
                val movie = getItem(position)
                it.movie = movie
                it.imageView.setOnClickListener {
                    transitionWithSharedElements(binding, movie)
                }
            }
        }

        private fun transitionWithSharedElements(binding: ItemMovieBinding ,movie: Movie) {
            activity?.let {  activity ->
                // アホっぽいけどtransitionするため1〜5まで用意する
                val options = ActivityOptions.makeSceneTransitionAnimation(
                        activity,
                        Pair.create(binding.favoritesStar as View, activity.getString(R.string.transition_favorite_star1)),
                        Pair.create(binding.favoritesStar as View, activity.getString(R.string.transition_favorite_star2)),
                        Pair.create(binding.favoritesStar as View, activity.getString(R.string.transition_favorite_star3)),
                        Pair.create(binding.favoritesStar as View, activity.getString(R.string.transition_favorite_star4)),
                        Pair.create(binding.favoritesStar as View, activity.getString(R.string.transition_favorite_star5)),
                        Pair.create(binding.imageView as View, activity.getString(R.string.transition_movie_image))
                )
                MovieDetailActivity.startForResult(this@ComingSoonMoviesFragment, movie.id, REQUEST_CODE_TO_DETAIL, options)
            } ?: Timber.e("activityがnullです。")
        }

        fun refresh(movie: Movie) {
            adapter.getItemPosition(movie)?.let { index ->
                adapter.getItem(index).update(movie)
                notifyItemChanged(index)
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_TO_DETAIL = 2000
        fun newInstance() = ComingSoonMoviesFragment()
    }
}