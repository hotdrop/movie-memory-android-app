package jp.hotdrop.moviememory.presentation.movie.detail

import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivityMovieDetailEditBinding
import jp.hotdrop.moviememory.presentation.BaseActivity
import kotlinx.android.synthetic.main.activity_movie_detail_edit.*
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

class MovieDetailEditActivity: BaseActivity() {

    private lateinit var binding: ActivityMovieDetailEditBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MovieDetailEditViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MovieDetailEditViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail_edit)

        getComponent().inject(this)

        initView()
        load()
    }

    private fun initView() {

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.movie.observe(this, Observer {
            it?.let { binding.movie = it }
        })

        binding.calendarImageIcon.setOnClickListener {
            showDatePickerDialog()
        }

        viewModel.saveSuccess.observe(this, Observer {
            it?.let {
                if (it) {
                    val message = getString(R.string.toast_message_save_success)
                    Toast.makeText(this@MovieDetailEditActivity, message, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        })

        fab.setOnClickListener {
            viewModel.saveMovie(binding.movieSawDateText.text.toString())
        }
    }

    private fun load() {
        val movieId = intent.getIntExtra(EXTRA_TAG, -1)
        viewModel.loadMovie(movieId)
    }

    private fun showDatePickerDialog() {
        //val sawDate = binding.movie?.sawDate ?: LocalDate.now()
        var sawDate = LocalDate.now()
        if (binding.movieSawDateText.text.isNotEmpty()) {
            sawDate = LocalDate.parse(binding.movieSawDateText.text)
        }
        DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    Timber.d("選択した日付 = $selectedDate")
                    binding.movieSawDateText.text = selectedDate.toString()
                }, sawDate.year, sawDate.monthValue - 1 ,sawDate.dayOfMonth
        ).show()
    }

    companion object {
        private const val EXTRA_TAG = "EXTRA_TAG"
        fun start(context: Context, movieId: Int) =
                context.startActivity(Intent(context, MovieDetailEditActivity::class.java).apply {
                    putExtra(EXTRA_TAG, movieId)
                })
    }
}
