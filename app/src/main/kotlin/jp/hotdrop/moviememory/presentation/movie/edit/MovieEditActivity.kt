package jp.hotdrop.moviememory.presentation.movie.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivityMovieEditBinding
import jp.hotdrop.moviememory.di.component.component
import jp.hotdrop.moviememory.presentation.BaseActivity

class MovieEditActivity: BaseActivity() {

    private val binding: ActivityMovieEditBinding by lazy {
        DataBindingUtil.setContentView<ActivityMovieEditBinding>(this, R.layout.activity_movie_edit)
    }

    private val viewModel: MovieEditViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MovieEditViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        observe()
        initView()
    }

    private fun observe() {
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

        val movieId = intent.getLongExtra(EXTRA_MOVIE_TAG, -1)
        val editType = intent.getSerializableExtra(EXTRA_EDIT_TYPE) as EditType

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
            title = when (editType) {
                EditType.OVERVIEW -> getString(R.string.movie_edit_overview_title)
                EditType.DETAIL -> getString(R.string.movie_edit_detail_title)
            }
        }

        when (editType) {
            EditType.OVERVIEW -> replaceFragment(MovieEditOverviewFragment.newInstance(movieId))
            EditType.DETAIL -> replaceFragment(MovieEditDetailFragment.newInstance(movieId))
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.transaction {
            replace(R.id.content_view, fragment)
        }
    }

    companion object {
        enum class EditType { OVERVIEW, DETAIL }
        private const val EXTRA_MOVIE_TAG = "EXTRA_MOVIE_TAG"
        private const val EXTRA_EDIT_TYPE = "EXTRA_EDIT_TYPE"
        fun startForResult(activity: Activity, movieId: Long, editType: EditType, requestCode: Int) =
                activity.startActivityForResult(Intent(activity, MovieEditActivity::class.java)
                        .apply {
                            putExtra(EXTRA_MOVIE_TAG, movieId)
                            putExtra(EXTRA_EDIT_TYPE, editType)
                        }, requestCode)
    }
}