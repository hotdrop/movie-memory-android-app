package jp.hotdrop.moviememory.presentation.movie

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentTabRootBinding
import jp.hotdrop.moviememory.model.MovieType
import jp.hotdrop.moviememory.presentation.BaseFragment
import jp.hotdrop.moviememory.presentation.movie.tab.TabMoviesFragment

class MoviesFragment: BaseFragment() {

    private lateinit var binding: FragmentTabRootBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTabRootBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.moviesViewPager.adapter = MoviesViewPagerAdapter(childFragmentManager).apply {
            setMovieTab()
        }

        binding.tabLayout.setupWithViewPager(binding.moviesViewPager)
    }

    /**
     * Tabのアダプター
     */
    private inner class MoviesViewPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {

        private val tabFragments = mutableListOf<MovieTab>()

        override fun getItem(position: Int): Fragment {
            tabFragments[position].let { tab ->
                return tab.fragment.also { fragment ->
                    fragment.arguments = Bundle().apply {
                        putSerializable(MovieType.ARGUMENT_TAG, tab.type)
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

    enum class MovieTab(val fragment: Fragment, @StringRes val titleRes: Int, val type: MovieType) {
        NowPlaying(TabMoviesFragment.newInstance(), R.string.tab_name_now_playing, MovieType.NowPlaying),
        ComingSoon(TabMoviesFragment.newInstance(), R.string.tab_name_coming_soon, MovieType.ComingSoon),
        Past(TabMoviesFragment.newInstance(), R.string.tab_name_past, MovieType.Past)
    }

    companion object {
        fun newInstance(): MoviesFragment = MoviesFragment()
    }
}
