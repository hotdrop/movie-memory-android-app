package jp.hotdrop.moviememory.presentation.movie.detail

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivityMovieDetailBinding
import jp.hotdrop.moviememory.presentation.BaseActivity

class MovieDetailActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMovieDetailBinding>(this, R.layout.activity_movie_detail)

        getComponent().inject(this)

        initView()
    }

    private fun initView() {
        val movieId = intent.getIntExtra(EXTRA_MOVIE_TAG, -1)
        showDetailFragment(movieId)
    }

    fun showDetailFragment(movieId: Int) {
        val fragment = MovieDetailFragment.newInstance(movieId)
        replaceFragment(fragment)
    }

    fun showEditFragment(movieId: Int) {
        val fragment = MovieDetailEditFragment.newInstance(movieId)
        // stackに積んだ方がいい
        replaceFragment(fragment)
    }

    fun startBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_view, fragment)
                .commit()
    }

    companion object {
        private const val EXTRA_MOVIE_TAG = "EXTRA_MOVIE_TAG"
        fun start(context: Context, movieId: Int) =
                context.startActivity(Intent(context, MovieDetailActivity::class.java).apply {
                    putExtra(EXTRA_MOVIE_TAG, movieId)
                })
    }
}
