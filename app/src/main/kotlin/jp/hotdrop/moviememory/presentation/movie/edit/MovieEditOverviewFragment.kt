package jp.hotdrop.moviememory.presentation.movie.edit

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
import jp.hotdrop.moviememory.databinding.FragmentMovieEditOverviewBinding
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.presentation.BaseFragment
import timber.log.Timber
import javax.inject.Inject

class MovieEditOverviewFragment: BaseFragment() {

    private lateinit var binding: FragmentMovieEditOverviewBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MovieEditViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory).get(MovieEditViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        getComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMovieEditOverviewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        observe()

        arguments?.getLong(EXTRA_MOVIE_ID)?.let {
            viewModel.find(it)
        }
    }

    private fun initView() {
        binding.fab.setOnClickListener {
            binding.movie?.let { movie ->
                viewModel.save(movie)
            }
        }
    }

    private fun observe() {
        viewModel.movie.observe(this, Observer {
            it?.let { movie ->
                binding.movie = movie
                viewModel.findCategories()
            }
        })
        viewModel.categories.observe(this, Observer {
            it?.let { categories ->
                initChipCategories(categories)
            }
        })
    }

    private fun initChipCategories(categories: List<Category>) {
        binding.chipGroupCategories.removeAllViews()

        categories.forEach { category ->
            val chip = (layoutInflater.inflate(R.layout.chip_category_choice, binding.chipGroupCategories, false) as Chip).apply {
                val categoryName = category.name
                text = categoryName
                setOnClickListener {
                    Timber.d("$categoryName を設定しました。")
                    binding.movie?.category = viewModel.findCategory(categoryName)
                }
                if (categoryName == binding.movie?.categoryName()) {
                    this.isChecked = true
                }
            }
            binding.chipGroupCategories.addView(chip)
        }
    }

    companion object {
        private const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"
        fun newInstance(movieId: Long): MovieEditOverviewFragment = MovieEditOverviewFragment().apply {
            arguments = Bundle().apply { putLong(EXTRA_MOVIE_ID, movieId) }
        }
    }
}