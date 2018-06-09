package jp.hotdrop.moviememory.presentation.movie.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Toast
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivityMovieDetailBinding
import jp.hotdrop.moviememory.presentation.BaseActivity
import jp.hotdrop.moviememory.presentation.NavigationController
import javax.inject.Inject

class MovieDetailActivity: BaseActivity() {

    @Inject
    lateinit var navigationController: NavigationController

    private lateinit var binding: ActivityMovieDetailBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MovieDetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MovieDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)

        getComponent().inject(this)

        initView()
        load()
    }

    private fun initView() {

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setOnClickEvent()

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
    }

    private fun setOnClickEvent() {

        fun startToWebLink(url: String?) {
            if (url.isNullOrEmpty()) {
                Toast.makeText(this, getString(R.string.movie_link_tap_non_url), Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }

        // TODO このfabは単なるお気に入りにする予定
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        binding.movieUrlLink.setOnClickListener {
            viewModel.movie.value?.let { startToWebLink(it.imageUrl) }
        }

        binding.officialUrlLink.setOnClickListener {
            viewModel.movie.value?.let { startToWebLink(it.url) }
        }

        binding.movieEditImage.setOnClickListener {
            binding.movie?.let { navigationController.navigationToMovieEdit(it.id) }
        }
    }

    private fun load() {
        val movieId = intent.getIntExtra(EXTRA_MOVIE_TAG, 0)
        viewModel.loadMovie(movieId)
    }

    companion object {
        private const val EXTRA_MOVIE_TAG = "EXTRA_MOVIE_TAG"
        fun start(context: Context, movieId: Int) =
                context.startActivity(Intent(context, MovieDetailActivity::class.java).apply {
                    putExtra(EXTRA_MOVIE_TAG, movieId)
                })
    }
}
