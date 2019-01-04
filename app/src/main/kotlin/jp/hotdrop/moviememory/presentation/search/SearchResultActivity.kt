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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivitySearchResultBinding
import jp.hotdrop.moviememory.databinding.ItemMovieBinding
import jp.hotdrop.moviememory.databinding.RowSuggestionBinding
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.model.SearchCondition
import jp.hotdrop.moviememory.model.Suggestion
import jp.hotdrop.moviememory.presentation.BaseActivity
import jp.hotdrop.moviememory.presentation.movie.detail.MovieDetailActivity
import jp.hotdrop.moviememory.presentation.parts.RecyclerViewAdapter
import javax.inject.Inject

class SearchResultActivity: BaseActivity() {

    private lateinit var binding: ActivitySearchResultBinding
    private var adapter: MoviesAdapter? = null
    private var suggestionAdapter: SuggestionAdapter? = null

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

        val searchCondition = intent.getSerializableExtra(EXTRA_SEARCH_CONDITION) as SearchCondition
        viewModel.prepared(searchCondition)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let { menuItem ->
            if (menuItem.itemId == R.id.action_suggestions_clear) {
                AlertDialog.Builder(this)
                        .setTitle(R.string.search_result_menu_suggestion_clear)
                        .setMessage(R.string.search_result_suggestion_clear_message)
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                            viewModel.clearSuggestions()
                        }
                        .setNegativeButton(android.R.string.cancel, null)
                        .show()
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

        // 検索キーワード履歴一覧の初期化
        binding.suggestionsRecyclerView.let {
            it.bringToFront()
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(this)
            suggestionAdapter = SuggestionAdapter()
            it.adapter = suggestionAdapter
        }

        // 検索結果一覧の初期化
        binding.moviesRecyclerView.let {
            it.layoutManager = GridLayoutManager(this, 2)
            adapter = MoviesAdapter()
            it.adapter = adapter
        }
    }

    private fun observe() {
        viewModel.prepared.observe(this, Observer {
            it?.let { prepared ->
                if (prepared) {
                    onSuccessPrepared()
                } else {
                    onFailurePrepared()
                }
            }
        })
        viewModel.suggestion.observe(this, Observer {
            it?.let { searchKeywords ->
                onLoadSearchKeywordHistory(searchKeywords)
            }
        })
        viewModel.clearedSuggestions.observe(this, Observer {
            it?.let { success ->
                if (success) {
                    Snackbar.make(binding.snackbarArea, "履歴をクリアしました。", Snackbar.LENGTH_SHORT).show()
                    viewModel.clear()
                }
            }
        })
        viewModel.movies.observe(this, Observer {
            it?.let { movies ->
                onLoadMovies(movies)
            }
        })
        viewModel.error.observe(this, Observer {
            it?.let { error ->
                Snackbar.make(binding.snackbarArea, error.getMessage() ?: "もう一度実行してください。", Snackbar.LENGTH_LONG).show()
                viewModel.clear()
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun onSuccessPrepared() {

        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.suggestionsRecyclerView.run {
                    isVisible = true
                    scrollToPosition(0)
                }
            } else {
                binding.suggestionsRecyclerView.isGone = true
            }
        }

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                filterSuggestion(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.run {
                    binding.searchView.clearFocus()
                    viewModel.find(query)
                    viewModel.save(query)
                    return true
                }
            }
        })

        binding.moviesRecyclerView.isVisible = true
        binding.progress.isGone = true
    }

    private fun onFailurePrepared() {
        binding.emptyMessage.isVisible = true
        binding.progress.isGone = true
    }

    private fun filterSuggestion(query: String?) {
        viewModel.suggestion.value?.let { suggestions ->
            if (query.isNullOrEmpty()) {
                onLoadSearchKeywordHistory(suggestions)
            } else {
                suggestions.filter {
                    it.keyword.startsWith(query)
                }.run {
                    onLoadSearchKeywordHistory(this)
                }
            }
        }
    }

    private fun onLoadSearchKeywordHistory(suggestions: List<Suggestion>) {
        suggestionAdapter?.refresh(suggestions)
    }

    private fun onLoadMovies(movies: List<Movie>) {
        adapter?.refresh(movies)
        binding.emptyMessage.isVisible = movies.isEmpty()
    }

    /**
     * キーワード履歴のアダプター
     */
    inner class SuggestionAdapter: RecyclerViewAdapter<Suggestion, RecyclerViewAdapter.BindingHolder<RowSuggestionBinding>>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<RowSuggestionBinding> =
                BindingHolder(parent, R.layout.row_suggestion)

        override fun onBindViewHolder(holder: BindingHolder<RowSuggestionBinding>, position: Int) {
            holder.binding?.let { rowBinding ->
                val keyword = getItem(position)
                rowBinding.keyword.text = keyword.keyword
                rowBinding.contentLayout.setOnClickListener {
                    binding.searchView.setQuery(keyword.keyword, true)
                }
            }
        }
    }

    /**
     * 検索結果の映画アダプター
     */
    inner class MoviesAdapter: RecyclerViewAdapter<Movie, RecyclerViewAdapter.BindingHolder<ItemMovieBinding>>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemMovieBinding> =
                BindingHolder(parent, R.layout.item_movie)

        override fun onBindViewHolder(holder: BindingHolder<ItemMovieBinding>, position: Int) {
            holder.binding?.let { itemBinding ->
                val movie = getItem(position)
                itemBinding.movie = movie
                itemBinding.imageView.setOnClickListener {
                    transitionWithSharedElements(itemBinding, movie)
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
    }

    companion object {
        private const val EXTRA_SEARCH_CONDITION = "EXTRA_SEARCH_CONDITION"
        private const val REQUEST_CODE_TO_DETAIL = 2000
        fun start(context: Context, condition: SearchCondition) =
                context.startActivity(Intent(context, SearchResultActivity::class.java).apply {
                    putExtra(EXTRA_SEARCH_CONDITION, condition)
                })
    }
}