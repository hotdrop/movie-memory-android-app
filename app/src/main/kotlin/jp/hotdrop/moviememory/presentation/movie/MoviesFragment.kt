package jp.hotdrop.moviememory.presentation.movie

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentMoviesBinding
import jp.hotdrop.moviememory.di.component.component
import jp.hotdrop.moviememory.model.MovieCondition
import jp.hotdrop.moviememory.presentation.movie.tab.TabMoviesFragment
import timber.log.Timber
import javax.inject.Inject

class MoviesFragment: Fragment() {

    private lateinit var binding: FragmentMoviesBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MoviesViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MoviesViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        activity?.component?.fragment()?.inject(this) ?: kotlin.run {
            Timber.d("onAttachが呼ばれましたがgetActivityがnullだったので終了します")
            onDestroy()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observe()
    }

    private fun initView() {
        binding.progress.isVisible = true
    }

    private fun observe() {
        viewModel.prepared.observe(viewLifecycleOwner, Observer {
            it?.run {
                binding.progress.isGone = true
                if (this) {
                    binding.moviesViewPager.adapter = MoviesViewPagerAdapter(childFragmentManager).apply {
                        setMovieTab()
                    }
                    binding.tabLayout.setupWithViewPager(binding.moviesViewPager)
                }
            }
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            it?.run {
                binding.progress.isGone = true
                val message = getString(R.string.message_failure_load_data)
                Snackbar.make(binding.moviesArea, message, Snackbar.LENGTH_LONG).show()
                viewModel.clear()
            }
        })
        lifecycle.addObserver(viewModel)
    }

    /**
     * Tabのアダプター
     */
    private inner class MoviesViewPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val tabFragments = mutableListOf<MovieTab>()

        override fun getItem(position: Int): Fragment {
            tabFragments[position].let { tab ->
                return tab.fragment.also { fragment ->
                    fragment.arguments = Bundle().apply {
                        putSerializable(MovieCondition.ARGUMENT_TAG, tab.condition)
                    }
                }
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            val titleRes = tabFragments[position].titleRes
            return this@MoviesFragment.getString(titleRes)
        }

        override fun getCount() = tabFragments.size

        fun setMovieTab() {
            tabFragments.clear()
            tabFragments.add(MovieTab.NowPlaying)
            tabFragments.add(MovieTab.ComingSoon)
            tabFragments.add(MovieTab.Past)
        }
    }

    enum class MovieTab(val fragment: Fragment, @StringRes val titleRes: Int, val condition: MovieCondition) {
        NowPlaying(TabMoviesFragment.newInstance(), R.string.tab_name_now_playing, MovieCondition.NowPlaying),
        ComingSoon(TabMoviesFragment.newInstance(), R.string.tab_name_coming_soon, MovieCondition.ComingSoon),
        Past(TabMoviesFragment.newInstance(), R.string.tab_name_past, MovieCondition.Past)
    }

    companion object {
        fun newInstance(): MoviesFragment = MoviesFragment()
    }
}
