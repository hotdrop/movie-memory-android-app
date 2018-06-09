package jp.hotdrop.moviememory.presentation.movie.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivityMovieDetailEditBinding
import jp.hotdrop.moviememory.presentation.BaseActivity
import kotlinx.android.synthetic.main.activity_movie_detail_edit.*
import javax.inject.Inject

class MovieDetailEditActivity: BaseActivity() {

    private lateinit var binding: ActivityMovieDetailEditBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MovieDetailEditViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MovieDetailEditViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail_edit)

        getComponent().inject(this)

        initView()
        load()
    }

    private fun initView() {

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.movie.observe(this, Observer {
            it?.let { binding.movie = it }
        })

        viewModel.saveSuccess.observe(this, Observer {
            it?.let {
                if (it) {
                    val message = getString(R.string.toast_message_save_success)
                    Toast.makeText(this@MovieDetailEditActivity, message, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        })

        fab.setOnClickListener {
            viewModel.saveMovie(binding.movieSawDateText.text.toString())
        }
    }

    private fun load() {
        val movieId = intent.getIntExtra(EXTRA_TAG, -1)
        viewModel.loadMovie(movieId)
    }

    companion object {
        private const val EXTRA_TAG = "EXTRA_TAG"
        fun start(context: Context, movieId: Int) =
                context.startActivity(Intent(context, MovieDetailEditActivity::class.java).apply {
                    putExtra(EXTRA_TAG, movieId)
                })
    }

}
