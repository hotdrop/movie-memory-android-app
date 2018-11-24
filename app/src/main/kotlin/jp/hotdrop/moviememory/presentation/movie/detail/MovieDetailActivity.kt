package jp.hotdrop.moviememory.presentation.movie.detail

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivityMovieDetailBinding
import jp.hotdrop.moviememory.presentation.BaseActivity
import jp.hotdrop.moviememory.presentation.component.FavoriteStars
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
    private var favoriteStars: FavoriteStars? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getComponent().inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)

        initView()
        observe()
    }

    override fun onResume() {
        super.onResume()
        favoriteStars?.run {
            viewModel.saveFavorite(this.count())
        }
    }

    private fun observe() {
        viewModel.setUp(movieId)
        viewModel.movie?.observe(this, Observer {
            it?.run {
                binding.movie = this
                initFavoriteStar(this.favoriteCount)
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

    private fun initFavoriteStar(favoriteCount: Int) {
        // お気に入りの数を取得してから初期化すべきのため、LiveDataのObserverの中で初期化している
        // そのため、一度初期化していた場合はスルーする。
        if (favoriteStars != null) {
            return
        }
        favoriteStars = FavoriteStars(
                listOf(binding.favorite1,
                        binding.favorite2,
                        binding.favorite3,
                        binding.favorite4,
                        binding.favorite5
                ), favoriteCount)
    }

    private fun startToWebLink(url: String) {
        if (url.startsWith("http")) {
            startBrowser(url)
        }
    }

    private fun navigationToEdit() {
        MovieDetailEditActivity.startForResult(this, movieId, MOVIE_DETAIL_REQUEST_CODE)
    }

    private fun startBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == MOVIE_DETAIL_REQUEST_CODE) {
            Snackbar.make(
                    binding.snackbarArea,
                    R.string.message_save_success,
                    Snackbar.LENGTH_SHORT
            ).also { snackbar ->
                snackbar.view.background = ContextCompat.getDrawable(this, R.drawable.shape_corner_circle)
            }.show()
        }
    }

    companion object {
        const val MOVIE_DETAIL_REQUEST_CODE = 1000
        private const val EXTRA_MOVIE_TAG = "EXTRA_MOVIE_TAG"
        fun start(context: Context, movieId: Int, options: ActivityOptions? = null) =
                context.startActivity(Intent(context, MovieDetailActivity::class.java)
                        .apply {
                            putExtra(EXTRA_MOVIE_TAG, movieId)
                        }, options?.toBundle())
    }
}
