package jp.hotdrop.moviememory.presentation.movie.edit

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import jp.hotdrop.moviememory.databinding.FragmentMovieEditMyNoteBinding
import jp.hotdrop.moviememory.presentation.BaseFragment
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

class MovieEditMyNoteFragment: BaseFragment() {

    private lateinit var binding: FragmentMovieEditMyNoteBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModel: MovieEditViewModel? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        getComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMovieEditMyNoteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProviders.of(it, viewModelFactory).get(MovieEditViewModel::class.java)
        } ?: endProcessBecauseIllegalState("activity is null on onActivityCreated")

        arguments?.getLong(EXTRA_MOVIE_ID)?.let { movieId ->

            initView()
            observe()

            viewModel?.find(movieId) ?: endProcessBecauseIllegalState("viewModel is null on onActivityCreated")
        }
    }

    private fun initView() {

        binding.watchDateEditArea.run {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showDatePickerDialog()
                }
            }
        }

        binding.fab.setOnClickListener {
            binding.movie?.let { movie ->
                val editWatchDateString = binding.watchDateEditArea.text.toString()
                if (editWatchDateString.isNotEmpty()) {
                    movie.setWatchDateFromText(editWatchDateString)
                }
                viewModel?.save(movie) ?: endProcessBecauseIllegalState("viewModel is null on fabClickListener")
            } ?: endProcessBecauseIllegalState("bind movie is null on fabClickListener")
        }
    }

    private fun observe() {

    }

    private fun showDatePickerDialog() {

        val sawDate = if (binding.watchDateEditArea.text.isNullOrEmpty()) {
            LocalDate.now()
        } else {
            LocalDate.parse(binding.watchDateEditArea.text)
        }

        // sawDateEditAreaはユーザーに編集させたくないので操作の後は必ずclearFocusする。
        // もしまたDatePickerを使う場面が出てきたらComponentとして切り出したい
        context?.run {
            DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                        Timber.d("選択した日付 = $selectedDate")
                        binding.watchDateEditArea.let {
                            it.setText(selectedDate.toString())
                            it.clearFocus()
                        }
                    }, sawDate.year, sawDate.monthValue - 1 ,sawDate.dayOfMonth
            ).let { datePickerDialog ->
                datePickerDialog.setOnCancelListener {
                    binding.watchDateEditArea.clearFocus()
                }
                datePickerDialog.show()
            }
        }
    }

    private fun endProcessBecauseIllegalState(message: String) {
        Timber.e(message)
        (activity as? MovieEditActivity)?.run {
            this.finishByIllegalState(message)
        }
    }

    companion object {
        private const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"
        fun newInstance(movieId: Long): MovieEditMyNoteFragment = MovieEditMyNoteFragment().apply {
            arguments = Bundle().apply { putLong(EXTRA_MOVIE_ID, movieId) }
        }
    }
}
