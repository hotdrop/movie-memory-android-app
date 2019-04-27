package jp.hotdrop.moviememory.presentation.movie.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.ActivityMovieEditBinding
import jp.hotdrop.moviememory.di.component.component
import jp.hotdrop.moviememory.presentation.BaseActivity
import javax.inject.Inject

class MovieEditActivity: BaseActivity() {

    private lateinit var binding: ActivityMovieEditBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MovieEditViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MovieEditViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_edit)

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

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
        }

        replaceFragment(MovieEditFragment.newInstance(movieId))
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.content_view, fragment)
        }
    }

    companion object {
        private const val EXTRA_MOVIE_TAG = "EXTRA_MOVIE_TAG"
        fun startForResult(activity: Activity, movieId: Long, requestCode: Int) =
                activity.startActivityForResult(Intent(activity, MovieEditActivity::class.java)
                        .apply {
                            putExtra(EXTRA_MOVIE_TAG, movieId)
                        }, requestCode)
    }
}