package jp.hotdrop.moviememory.presentation.movie.nowplaying

import android.app.ActivityOptions
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentNowPlayingMoviesBinding
import jp.hotdrop.moviememory.databinding.ItemMovieBinding
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.presentation.MainActivity
import jp.hotdrop.moviememory.presentation.component.MovieFragmentWithEndlessRecyclerView
import jp.hotdrop.moviememory.presentation.parts.RecyclerViewAdapter
import timber.log.Timber
import javax.inject.Inject

class NowPlayingMoviesFragment: MovieFragmentWithEndlessRecyclerView() {

    private lateinit var binding: FragmentNowPlayingMoviesBinding
    private lateinit var adapter: NowPlayingMoviesAdapter
    private var activity: MainActivity? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: NowPlayingMoviesViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(NowPlayingMoviesViewModel::class.java)
    }

    // TODO もっといいやり方がないか模索する・・これモデルにしてステータス更新は全部そいつに任せたほうがいい
    private var nowObserveState = ObserveState.Normal
    private enum class ObserveState {
        Normal,  // 一覧に、取得したアイテムを追加していく
        Refresh, // 一覧のアイテムを全部クリアして、取得したアイテムをセットする
        OneStop  // 何もしない。onResumeでLiveDataがActiveになってしまうのでこれで止める
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        getComponent().inject(this)
        (getActivity() as? MainActivity)?.let {
            activity = it
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNowPlayingMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observe()
        initView()
    }

    private fun observe() {
        viewModel.movies.observe(this, Observer {
            it?.let { movies ->
                binding.nowPlayingProgress.visibility = View.GONE
                when (nowObserveState) {
                    ObserveState.Normal -> adapter.addAll(movies)
                    ObserveState.Refresh -> adapter.refresh(movies)
                    ObserveState.OneStop -> Timber.i("画面更新しない")
                }
                nowObserveState = ObserveState.Normal
            }
        })
        viewModel.error.observe(this, Observer {
            it?.let {
                val message = getString(R.string.message_failure_load_data)
                Snackbar.make(binding.nowPlayingMovieArea, message, Snackbar.LENGTH_LONG).show()
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun initView() {
        super.setupRecyclerView(binding.nowPlayingMoviesRecyclerView) { page, _ ->
            viewModel.onLoad(page)
        }
        adapter = NowPlayingMoviesAdapter()
        binding.nowPlayingMoviesRecyclerView.adapter = adapter

        super.setupSwipeRefresh(binding.nowPlayingSwipeRefresh) {
            nowObserveState = ObserveState.Refresh
            viewModel.onRefresh()
        }
    }

    companion object {
        fun newInstance() = NowPlayingMoviesFragment()
    }

    /**
     * アダプター
     */
    inner class NowPlayingMoviesAdapter: RecyclerViewAdapter<Movie, RecyclerViewAdapter.BindingHolder<ItemMovieBinding>>() {

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
                val options = ActivityOptions.makeSceneTransitionAnimation(
                        activity,
                        Pair.create(binding.imageView, activity.getString(R.string.transition_movie_image))
                )
                activity.navigationToMovieDetail(movie.id, options)
            } ?: Timber.e("activityがnullです。")
        }
    }
}