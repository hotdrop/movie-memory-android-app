package jp.hotdrop.moviememory.presentation.movie.detail

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.snackbar.Snackbar
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivityMovieDetailBinding
import jp.hotdrop.moviememory.databinding.ItemCastBinding
import jp.hotdrop.moviememory.di.component.component
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.presentation.BaseActivity
import jp.hotdrop.moviememory.presentation.component.FavoriteStars
import jp.hotdrop.moviememory.presentation.movie.edit.MovieEditActivity
import jp.hotdrop.moviememory.presentation.common.RecyclerViewAdapter
import jp.hotdrop.moviememory.presentation.component.TextInputDatePickerDialog
import jp.hotdrop.moviememory.presentation.component.TextInputDialog
import javax.inject.Inject

class MovieDetailActivity: BaseActivity() {

    private lateinit var binding: ActivityMovieDetailBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MovieDetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MovieDetailViewModel::class.java)
    }

    private val movieId: Long by lazy {
        intent.getLongExtra(EXTRA_MOVIE_TAG, -1)
    }

    private var favoriteStars: FavoriteStars? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)

        initView()
        observe()
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

        binding.fabEdit.setOnClickListener {
            MovieEditActivity.startForResult(this, movieId, MOVIE_EDIT_REQUEST_CODE)
        }

        setOnMyNoteClickListener()
    }

    private fun setOnMyNoteClickListener() {

        binding.watchDateArea.setOnClickListener {
            TextInputDatePickerDialog.show(this, binding.watchDateText.text.toString()) { selectedDate ->
                binding.watchDateText.text = selectedDate.toString()
                viewModel.saveWatchDate(selectedDate)
            }
        }

        binding.watchPlaceArea.setOnClickListener {
            TextInputDialog.Builder(this)
                    .setTitle(R.string.watch_place_label)
                    .setText(binding.watchPlaceText.text.toString())
                    .setOnPositiveListener {watchPlace ->
                        binding.watchPlaceText.text = watchPlace
                        viewModel.saveWatchPlace(watchPlace)
                    }.show()
        }

        binding.watchNoteArea.setOnClickListener {
            TextInputDialog.Builder(this)
                    .setTitle(R.string.watch_note_label)
                    .setText(binding.watchNoteText.text.toString())
                    .setOnPositiveListener { note ->
                        binding.watchNoteText.text = note
                        viewModel.saveWatchNote(note)
                    }.showWithInputMultiLine()
        }
    }

    private fun observe() {
        viewModel.setUp(movieId)
        viewModel.movie?.observe(this, Observer {
            it?.run {
                initViewAfterGetMovie(this)
            }
        })
        viewModel.isRefreshMovie.observe(this, Observer {
            it?.run {
                if (this) {
                    // スター数が変更されたので呼び元に通知するためのIntentを準備する
                    onResultRefreshMovie()
                }
            }
        })
        viewModel.error.observe(this, Observer {
            it?.run {
                Snackbar.make(binding.snackbarArea, R.string.movie_edit_error, Snackbar.LENGTH_LONG).show()
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun startToWebLink(url: String) {
        if (url.startsWith("http")) {
            startBrowser(url)
        }
    }

    private fun startBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun initViewAfterGetMovie(movie: Movie) {

        binding.movie = movie

        // お気に入りスター
        initFavoriteStar(movie.favoriteCount)

        // キャスト
        val castsRecyclerView = binding.castsEditArea.findViewById<RecyclerView>(R.id.casts_recycler_view)
        val emptyTextView = binding.castsEditArea.findViewById<TextView>(R.id.casts_empty_message)
        movie.casts?.also { casts ->
            castsRecyclerView.layoutManager = FlexboxLayoutManager(this).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
            }
            castsRecyclerView.adapter = CastsAdapter().apply { addAll(casts.map { "${it.name}:${it.actor}" }) }
            castsRecyclerView.isVisible = true
            emptyTextView.isGone = true
        } ?: run {
            castsRecyclerView.isGone = true
            emptyTextView.isVisible = true
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
                ), favoriteCount) { viewModel.saveFavorite(it) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == MOVIE_EDIT_REQUEST_CODE) {
            Snackbar.make(binding.snackbarArea, R.string.message_save_success, Snackbar.LENGTH_SHORT).show()
            onResultRefreshMovie()
        }
    }

    private fun onResultRefreshMovie() {
        binding.movie?.let { movie ->
            setResult(RESULT_OK, Intent().apply {
                putExtra(EXTRA_MOVIE_TAG, movie.id)
            })
        }
    }

    inner class CastsAdapter: RecyclerViewAdapter<String, RecyclerViewAdapter.BindingHolder<ItemCastBinding>>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemCastBinding> =
                BindingHolder(parent, R.layout.item_cast)

        override fun onBindViewHolder(holder: BindingHolder<ItemCastBinding>, position: Int) {
            val holderBinding = holder.binding
            holderBinding?.let { binding ->
                val cast = getItem(position)
                binding.castName.text = cast
            }
        }
    }

    companion object {

        const val MOVIE_EDIT_REQUEST_CODE = 900
        const val EXTRA_MOVIE_TAG = "EXTRA_MOVIE_TAG"

        fun startForResult(fragment: Fragment, movieId: Long, requestCode: Int, options: ActivityOptions? = null) =
                fragment.startActivityForResult(Intent(fragment.context, MovieDetailActivity::class.java)
                        .apply {
                            putExtra(EXTRA_MOVIE_TAG, movieId)
                        }, requestCode, options?.toBundle())
        fun startForResult(activity: Activity, movieId: Long, requestCode: Int, options: ActivityOptions? = null) =
                activity.startActivityForResult(Intent(activity, MovieDetailActivity::class.java)
                        .apply {
                            putExtra(EXTRA_MOVIE_TAG, movieId)
                        }, requestCode, options?.toBundle())
    }
}
