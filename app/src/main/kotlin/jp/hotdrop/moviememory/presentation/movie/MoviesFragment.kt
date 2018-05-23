package jp.hotdrop.moviememory.presentation.movie

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.hotdrop.moviememory.databinding.FragmentMoviesBinding
import jp.hotdrop.moviememory.presentation.BaseFragment

class MoviesFragment: BaseFragment() {

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var viewPagerAdapter: MoviesViewPagerAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        getComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO 検索アイコン作る
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerAdapter = MoviesViewPagerAdapter(childFragmentManager)
        viewPagerAdapter.setMovieTab()
        binding.moviesViewPager.adapter = viewPagerAdapter

        binding.tabLayout.setupWithViewPager(binding.moviesViewPager)
    }

    companion object {
        fun newInstance(): MoviesFragment = MoviesFragment()
    }

    private inner class MoviesViewPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {

        private val tabFragments = mutableListOf<MovieTab>()

        override fun getItem(position: Int): Fragment = tabFragments[position].fragment

        override fun getPageTitle(position: Int): CharSequence? {
            val titleRes = tabFragments[position].titleRes
            return this@MoviesFragment.getString(titleRes)
        }

        override fun getCount() = tabFragments.size

        fun setMovieTab() {
            tabFragments.clear()
            tabFragments.add(MovieTab.NowPlaying)
            //tabFragments.add(MovieTab.ComingSoon)
        }
    }
}
