package jp.hotdrop.moviememory.presentation.movie.detail

import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivityMovieDetailBinding
import jp.hotdrop.moviememory.presentation.BaseActivity
import javax.inject.Inject

class MovieDetailActivity: BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MovieDetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MovieDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityMovieDetailBinding>(this, R.layout.activity_movie_detail)
        getComponent().inject(this)

        initView()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun initView() {
        intent.getIntExtra(EXTRA_MOVIE_TAG, -1).let { movieId ->
            viewModel.setUp(movieId)
        }
        lifecycle.addObserver(viewModel)

        showDetailFragment()
    }

    private fun showDetailFragment() {
        replaceFragment(MovieDetailFragment.newInstance())
    }

    fun showEditFragment() {
        replaceFragment(MovieDetailEditFragment.newInstance())
    }

    fun startBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.transaction {
            replace(R.id.content_view, fragment)
            addToBackStack(null)
        }
    }

    companion object {
        private const val EXTRA_MOVIE_TAG = "EXTRA_MOVIE_TAG"
        fun start(context: Context, movieId: Int) =
                context.startActivity(Intent(context, MovieDetailActivity::class.java).apply {
                    putExtra(EXTRA_MOVIE_TAG, movieId)
                })
    }
}
