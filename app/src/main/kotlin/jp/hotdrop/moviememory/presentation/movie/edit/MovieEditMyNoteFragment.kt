package jp.hotdrop.moviememory.presentation.movie.edit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import jp.hotdrop.moviememory.databinding.FragmentMovieEditMyNoteBinding
import jp.hotdrop.moviememory.di.component.component
import jp.hotdrop.moviememory.presentation.component.TextInputDatePickerDialog
import org.threeten.bp.LocalDate
import timber.log.Timber
import java.lang.IllegalStateException
import javax.inject.Inject

class MovieEditMyNoteFragment: Fragment() {

    private lateinit var binding: FragmentMovieEditMyNoteBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
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
        binding = FragmentMovieEditMyNoteBinding.inflate(layoutInflater, container, false)
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
        binding.watchDateEditArea.run {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && context != null) {
//                    TextInputDatePickerDialog.show(context, binding.watchDateEditArea)
                }
            }
        }

        binding.fab.setOnClickListener {
            binding.movie?.let { movie ->

                val newWatchDateText = binding.watchDateEditArea.text.toString()
                val newWatchDate = if (newWatchDateText.isNotEmpty()) {
                    LocalDate.parse(newWatchDateText)
                } else {
                    movie.watchDate
                }

                val newMovie = movie.copy(watchDate = newWatchDate)
                viewModel?.saveMyNote(newMovie) ?: throw IllegalStateException("viewModel is null!!")
            }
        }
    }

    private fun observe() {
        viewModel?.movie?.observe(this, Observer {
            it?.let { movie ->
                binding.movie = movie
            }
        }) ?: throw IllegalStateException("viewModel is null!!")
    }

    companion object {
        private const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"
        fun newInstance(movieId: Long): MovieEditMyNoteFragment = MovieEditMyNoteFragment().apply {
            arguments = Bundle().apply { putLong(EXTRA_MOVIE_ID, movieId) }
        }
    }
}
