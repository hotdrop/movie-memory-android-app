package jp.hotdrop.moviememory.presentation.movie.detail

import android.app.DatePickerDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentMovieDetailEditBinding
import jp.hotdrop.moviememory.model.Movie
import jp.hotdrop.moviememory.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_movie_detail_edit.*
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

class MovieDetailEditFragment: BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MovieDetailEditViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(MovieDetailEditViewModel::class.java) }
    private lateinit var binding: FragmentMovieDetailEditBinding
    private val movieId by lazy { arguments?.getInt(EXTRA_TAG) ?: Movie.ILLEGAL_MOVIE_ID }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        getComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMovieDetailEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        load()
    }

    private fun initView() {

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowTitleEnabled(false)
            }
        }

        viewModel.movie.observe(this, Observer {
            it?.let { binding.movie = it }
        })

        binding.calendarImageIcon.setOnClickListener {
            showDatePickerDialog()
        }

        viewModel.saveSuccess.observe(this, Observer {
            it?.let {
                if (it) {
                    Toast.makeText(activity, getString(R.string.toast_message_save_success), Toast.LENGTH_SHORT).show()
                    (activity as MovieDetailActivity).showDetailFragment(movieId)
                }
            }
        })

        fab.setOnClickListener {
            viewModel.saveMovie(binding.movieSawDateText.text.toString())
        }
    }

    private fun load() {
        viewModel.loadMovie(movieId)
    }

    private fun showDatePickerDialog() {
        var sawDate = LocalDate.now()
        if (binding.movieSawDateText.text.isNotEmpty()) {
            sawDate = LocalDate.parse(binding.movieSawDateText.text)
        }
        DatePickerDialog(
                activity,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    Timber.d("選択した日付 = $selectedDate")
                    binding.movieSawDateText.text = selectedDate.toString()
                }, sawDate.year, sawDate.monthValue - 1 ,sawDate.dayOfMonth
        ).show()
    }

    companion object {
        private const val EXTRA_TAG = "EXTRA_TAG"
        fun newInstance(movieId: Int) = MovieDetailEditFragment().apply {
            arguments = Bundle().apply { putInt(EXTRA_TAG, movieId) }
        }
    }
}
