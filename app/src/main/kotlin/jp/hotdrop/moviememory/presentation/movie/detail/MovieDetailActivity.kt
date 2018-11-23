package jp.hotdrop.moviememory.presentation.movie.detail

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivityMovieDetailBinding
import jp.hotdrop.moviememory.presentation.BaseActivity
import javax.inject.Inject

class MovieDetailActivity: BaseActivity() {

    private lateinit var binding: ActivityMovieDetailBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MovieDetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MovieDetailViewModel::class.java)
    }

    private val movieId: Int by lazy {
        intent.getIntExtra(EXTRA_MOVIE_TAG, -1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getComponent().inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)

        observe()
        initView()
    }

    private fun observe() {
        viewModel.setUp(movieId)
        viewModel.movie?.observe(this, Observer {
            it?.run {
                binding.movie = this
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun initView() {

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        binding.trailerUrlLink.setOnClickListener {
            binding.movie?.trailerMovieUrl?.run {
                startToWebLink(this)
            }
        }

        binding.officialUrlLink.setOnClickListener {
            binding.movie?.officialUrl?.run {
                startToWebLink(this)
            }
        }

        binding.editFab.setOnClickListener {
            navigationToEdit()
        }
    }

    private fun startToWebLink(url: String) {
        if (url.startsWith("http")) {
            startBrowser(url)
        }
    }

    private fun navigationToEdit() {
        MovieDetailEditActivity.start(this, movieId)
    }

    private fun startBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    companion object {
        private const val EXTRA_MOVIE_TAG = "EXTRA_MOVIE_TAG"
        fun start(context: Context, movieId: Int, options: ActivityOptions? = null) =
                context.startActivity(Intent(context, MovieDetailActivity::class.java)
                        .apply {
                            putExtra(EXTRA_MOVIE_TAG, movieId)
                        }, options?.toBundle())
    }
}
