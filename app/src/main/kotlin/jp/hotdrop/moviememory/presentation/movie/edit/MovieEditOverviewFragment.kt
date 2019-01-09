package jp.hotdrop.moviememory.presentation.movie.edit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentMovieEditOverviewBinding
import jp.hotdrop.moviememory.di.component.component
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.presentation.BaseFragment
import timber.log.Timber
import java.lang.IllegalStateException

class MovieEditOverviewFragment: BaseFragment() {

    private lateinit var binding: FragmentMovieEditOverviewBinding

    private var viewModel: MovieEditViewModel? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        activity?.let {
            it.component.fragment().inject(this)
            viewModel = ViewModelProviders.of(it, viewModelFactory).get(MovieEditViewModel::class.java)
        } ?: kotlin.run {
            Timber.d("onAttachが呼ばれましたがgetActivityがnullだったので終了します")
            onDestroy()
        }
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
            viewModel?.find(it) ?: throw IllegalStateException("viewModel is null!!")
        }
    }

    private fun initView() {
        // TODO ImageUrlもこの画面で修正したい。でもどうやるか考える
        binding.fab.setOnClickListener {
            binding.movie?.let { movie ->
                viewModel?.save(movie) ?: throw IllegalStateException("viewModel is null!!")
            }
        }
    }

    private fun observe() {
        viewModel?.movie?.observe(this, Observer {
            it?.let { movie ->
                binding.movie = movie
                viewModel?.findCategories() ?: throw IllegalStateException("viewModel is null!!")
            }
        }) ?: throw IllegalStateException("viewModel is null!!")
        viewModel?.categories?.observe(this, Observer {
            it?.let { categories ->
                initChipCategories(categories)
            }
        }) ?: throw IllegalStateException("viewModel is null!!")
    }

    private fun initChipCategories(categories: List<Category>) {
        binding.chipGroupCategories.removeAllViews()

        categories.forEach { category ->
            val chip = (layoutInflater.inflate(R.layout.chip_category_choice, binding.chipGroupCategories, false) as Chip)
                    .apply {
                        val categoryName = category.name
                        text = categoryName
                        setOnClickListener {
                            viewModel?.stockCategory(categoryName) ?: throw IllegalStateException("viewModel is null!!")
                            (0 until binding.chipGroupCategories.childCount).forEach { idx ->
                                val chip = binding.chipGroupCategories[idx] as Chip
                                if (chip.text != categoryName) {
                                    chip.isChecked = false
                                }
                            }
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