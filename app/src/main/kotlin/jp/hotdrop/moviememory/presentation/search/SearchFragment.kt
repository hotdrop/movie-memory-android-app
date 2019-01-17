package jp.hotdrop.moviememory.presentation.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentSearchBinding
import jp.hotdrop.moviememory.di.component.component
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.model.SearchCondition
import timber.log.Timber
import javax.inject.Inject

class SearchFragment: Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var parentActivity: Context

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SearchViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        activity?.also {
            it.component.fragment().inject(this)
            parentActivity = it
        } ?: kotlin.run {
            Timber.d("onAttachが呼ばれましたがgetActivityがnullだったので終了します")
            onDestroy()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        observe()

        viewModel.findCategories()
    }

    private fun initView() {
        binding.favoriteImageIcon.progress = 1f

        binding.keywordArea.setOnClickListener {
            navigationToSearchResult(SearchCondition.Keyword())
        }

        binding.favoriteArea.setOnClickListener {
            navigationToSearchResult(SearchCondition.Favorite(1))
        }
    }

    private fun observe() {
        viewModel.categories.observe(this, Observer {
            it?.let { categories ->
                initChipCategories(categories)
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun initChipCategories(categories: List<Category>) {
        binding.chipGroupCategories.removeAllViews()
        categories.forEach { category ->
            val chip = (layoutInflater.inflate(R.layout.chip_category_action, binding.chipGroupCategories, false) as Chip).apply {
                text = category.name
                setOnClickListener {
                    navigationToSearchResult(SearchCondition.Category(category))
                }
            }
            binding.chipGroupCategories.addView(chip)
        }
    }

    private fun navigationToSearchResult(condition: SearchCondition) {
        SearchResultActivity.start(parentActivity, condition)
    }

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }
}