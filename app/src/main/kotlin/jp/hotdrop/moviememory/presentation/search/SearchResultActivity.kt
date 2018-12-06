package jp.hotdrop.moviememory.presentation.search

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.util.Pair
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivitySearchResultBinding
import jp.hotdrop.moviememory.databinding.ItemMovieBinding
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.model.SearchKeyword
import jp.hotdrop.moviememory.presentation.BaseActivity
import jp.hotdrop.moviememory.presentation.movie.detail.MovieDetailActivity
import jp.hotdrop.moviememory.presentation.parts.RecyclerViewAdapter
import javax.inject.Inject

class SearchResultActivity: BaseActivity() {

    private lateinit var binding: ActivitySearchResultBinding
    private var adapter: MoviesAdapter? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SearchResultViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchResultViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getComponent().inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_result)

        initView()
        observe()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let { menuItem ->
            when (menuItem.itemId) {
                R.id.action_suggestions_clear -> {
                    // TODO 履歴クリア処理
                    Snackbar.make(binding.snackbarArea, "履歴クリアは未実装です", Snackbar.LENGTH_LONG).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {

        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
        }

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                // 入力するたびに検索をするのはうざいだけなので一旦実装しない。
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.run {
                    binding.searchView.clearFocus()
                    // ワードをsuggestion登録する
                    viewModel.findByKeyword(SearchKeyword(query))
                    return true
                }
            }
        })

        // 検索結果一覧の初期化
        binding.moviesRecyclerView.let {
            val gridLayoutManager = GridLayoutManager(this, 2)
            it.layoutManager = gridLayoutManager
            adapter = MoviesAdapter()
            it.adapter = adapter
        }
    }

    private fun observe() {
        // TODO suggestionを取得する
        viewModel.movies.observe(this, Observer {
            it?.let { movies ->
                onLoadMovies(movies)
            }
        })
        viewModel.error.observe(this, Observer {
            it?.let { error ->
                Snackbar.make(binding.snackbarArea, error.getMessage() ?: "もう一度実行してください。", Snackbar.LENGTH_SHORT).show()
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun onLoadMovies(movies: List<Movie>) {

        adapter?.clearWithRefresh()

        if (movies.isEmpty()) {
            binding.emptyMessage.isVisible = true
            return
        }

        binding.emptyMessage.isGone = true
        adapter?.run {
            this.addAll(movies)
        }
    }

    inner class MoviesAdapter: RecyclerViewAdapter<Movie, RecyclerViewAdapter.BindingHolder<ItemMovieBinding>>() {

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
            this@SearchResultActivity.let {  activity ->
                // アホっぽいけどtransitionするため1〜5まで用意する
                val options = ActivityOptions.makeSceneTransitionAnimation(
                        activity,
                        Pair.create(binding.favoritesStar as View, activity.getString(R.string.transition_favorite_star1)),
                        Pair.create(binding.favoritesStar as View, activity.getString(R.string.transition_favorite_star2)),
                        Pair.create(binding.favoritesStar as View, activity.getString(R.string.transition_favorite_star3)),
                        Pair.create(binding.favoritesStar as View, activity.getString(R.string.transition_favorite_star4)),
                        Pair.create(binding.favoritesStar as View, activity.getString(R.string.transition_favorite_star5)),
                        Pair.create(binding.imageView as View, activity.getString(R.string.transition_movie_image))
                )
                MovieDetailActivity.startForResult(this@SearchResultActivity, movie.id, REQUEST_CODE_TO_DETAIL, options)
            }
        }

        fun refresh(movie: Movie) {
            adapter?.let { adapter ->
                adapter.getItemPosition(movie)?.let { index ->
                    adapter.getItem(index).update(movie)
                    notifyItemChanged(index)
                }
            }
        }

        fun clearWithRefresh() {
            clear()
            notifyDataSetChanged()
        }
    }

    companion object {
        private const val REQUEST_CODE_TO_DETAIL = 2000
        fun start(context: Context) =
                context.startActivity(Intent(context, SearchResultActivity::class.java))
    }
}