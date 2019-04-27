package jp.hotdrop.moviememory.presentation.movie.edit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentMovieEditBinding
import jp.hotdrop.moviememory.di.component.component
import jp.hotdrop.moviememory.model.AppDate
import jp.hotdrop.moviememory.model.Category
import jp.hotdrop.moviememory.presentation.component.TextInputDatePickerDialog
import timber.log.Timber
import java.lang.IllegalStateException
import javax.inject.Inject

class MovieEditFragment: Fragment() {

    private lateinit var binding: FragmentMovieEditBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModel: MovieEditViewModel? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        activity?.also {
            it.component.fragment().inject(this)
            viewModel = ViewModelProviders.of(it, viewModelFactory).get(MovieEditViewModel::class.java)
        } ?: run {
            Timber.d("onAttachが呼ばれましたがgetActivityがnullだったので終了します")
            onDestroy()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMovieEditBinding.inflate(layoutInflater, container, false)
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

        binding.playDateEditArea.run {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    TextInputDatePickerDialog.show(context, binding.playDateEditArea)
                }
            }
        }

        binding.fab.setOnClickListener {
            binding.movie?.let { movie ->
                val newPlayingDateText = binding.playDateEditArea.text.toString()
                val newPlayingDate = if (newPlayingDateText.isNotEmpty()) {
                    AppDate(dateStr = newPlayingDateText)
                } else {
                    movie.playingDate
                }
                val newMovie = movie.copy(playingDate = newPlayingDate)

                viewModel?.save(newMovie) ?: throw IllegalStateException("viewModel is null!!")
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
            val chip = (layoutInflater.inflate(R.layout.chip_category_filter, binding.chipGroupCategories, false) as Chip)
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
        fun newInstance(movieId: Long): MovieEditFragment = MovieEditFragment().apply {
            arguments = Bundle().apply { putLong(EXTRA_MOVIE_ID, movieId) }
        }
    }
}