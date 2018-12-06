package jp.hotdrop.moviememory.presentation.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentSearchBinding
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.presentation.BaseFragment
import jp.hotdrop.moviememory.presentation.MainActivity
import javax.inject.Inject

class SearchFragment: BaseFragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var activity: MainActivity

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SearchViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getComponent().inject(this)
        (context as MainActivity).let {
            activity = it
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
            SearchResultActivity.start(activity)
        }

        binding.favoriteArea.setOnClickListener {
            // TODO
        }
    }

    private fun observe() {
        viewModel.categories.observe(this, Observer {
            it?.let {
                initChipCategories(it)
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun initChipCategories(categories: List<Category>) {
        binding.chipGroupCategories.removeAllViews()
        categories.forEach { category ->
            val chip = (layoutInflater.inflate(R.layout.chip_category, binding.chipGroupCategories, false) as Chip).apply {
                text = category.name
                setOnClickListener {
                    // TODO
                }
            }
            binding.chipGroupCategories.addView(chip)
        }
    }

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }
}