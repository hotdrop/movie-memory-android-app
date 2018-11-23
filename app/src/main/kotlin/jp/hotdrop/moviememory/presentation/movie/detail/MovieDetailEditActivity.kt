package jp.hotdrop.moviememory.presentation.movie.detail

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivityMovieDetailEditBinding
import jp.hotdrop.moviememory.presentation.BaseActivity
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

    private val movieId: Int by lazy {
        intent.getIntExtra(EXTRA_MOVIE_TAG, -1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getComponent().inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail_edit)

        observe()
        initView()
    }

    private fun observe() {
        viewModel.setUp(movieId)
        viewModel.movie?.observe(this, Observer {
            it?.run {
                binding.movie = this
            }
        })
        viewModel.saveSuccess.observe(this, Observer {
            it?.let { success ->
                if (success) {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun initView() {

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        binding.watchDateEditArea.run {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showDatePickerDialog()
                }
            }
        }

        binding.fab.setOnClickListener {
            viewModel.save(binding.watchDateEditArea.text.toString())
        }
    }

    private fun showDatePickerDialog() {

        val sawDate = if (binding.watchDateEditArea.text.isNullOrEmpty()) {
            LocalDate.now()
        } else {
            LocalDate.parse(binding.watchDateEditArea.text)
        }

        // sawDateEditAreaはユーザーに編集させたくないので操作の後は必ずclearFocusする。
        // もしまたDatePickerを使う場面が出てきたらComponentとして切り出したい
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

    companion object {
        private const val EXTRA_MOVIE_TAG = "EXTRA_MOVIE_TAG"
        fun startForResult(activity: Activity, movieId: Int, requestCode: Int) =
                activity.startActivityForResult(Intent(activity, MovieDetailEditActivity::class.java)
                        .apply {
                            putExtra(EXTRA_MOVIE_TAG, movieId)
                        }, requestCode)
    }
}