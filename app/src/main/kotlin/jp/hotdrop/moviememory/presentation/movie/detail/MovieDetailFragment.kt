package jp.hotdrop.moviememory.presentation.movie.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentMovieDetailBinding
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.presentation.BaseFragment
import javax.inject.Inject

class MovieDetailFragment: BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MovieDetailViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(MovieDetailViewModel::class.java) }
    private lateinit var binding: FragmentMovieDetailBinding
    private val movieId by lazy { arguments?.getInt(EXTRA_TAG) ?: Movie.ILLEGAL_MOVIE_ID }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        getComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        load()
    }

    private fun initView() {

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowTitleEnabled(false)
            }
        }

        // Droidkaigi2018のCoordinatorLayoutの動きが素晴らしかったので真似ました。
        binding.appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val factor = (-verticalOffset).toFloat() / appBarLayout.totalScrollRange.toFloat()
            binding.toolbarTextColorFactor = factor
        }

        viewModel.movie.observe(this, Observer {
            it?.let {
                binding.movie = it
            }
        })
        lifecycle.addObserver(viewModel)

        setOnClickEvent()
    }

    private fun setOnClickEvent() {

        fun startToWebLink(url: String?) {
            if (url.isNullOrEmpty()) {
                Toast.makeText(activity, getString(R.string.movie_link_tap_non_url), Toast.LENGTH_SHORT).show()
            } else {
                (activity as MovieDetailActivity).startBrowser(url!!)
            }
        }

        // TODO このfabは単なるお気に入りにする予定
        binding.fab.setOnClickListener { view ->
            TODO()
        }

        binding.movieUrlLink.setOnClickListener {
            viewModel.movie.value?.let { startToWebLink(it.imageUrl) }
        }

        binding.officialUrlLink.setOnClickListener {
            viewModel.movie.value?.let { startToWebLink(it.url) }
        }

        binding.movieEditImage.setOnClickListener {
            binding.movie?.let { (activity as MovieDetailActivity).showEditFragment(it.id) }
        }
    }

    private fun load() {
        viewModel.loadMovie(movieId)
    }

    companion object {
        private const val EXTRA_TAG = "EXTRA_MOVIE_DETAIL_TAG"
        fun newInstance(movieId: Int) = MovieDetailFragment().apply {
            arguments = Bundle().apply { putInt(EXTRA_TAG, movieId) }
        }
    }
}