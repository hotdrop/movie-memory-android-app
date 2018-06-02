package jp.hotdrop.moviememory.presentation.movie.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivityMovieDetailBinding
import jp.hotdrop.moviememory.presentation.BaseActivity
import kotlinx.android.synthetic.main.activity_movie_detail.*
import javax.inject.Inject

class MovieDetailActivity: BaseActivity() {

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

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        viewModel.movie.observe(this, Observer {
            it?.let {
                binding.movie = it
            }
        })
        lifecycle.addObserver(viewModel)
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
