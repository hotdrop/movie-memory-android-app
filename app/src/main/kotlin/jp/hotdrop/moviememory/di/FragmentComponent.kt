package jp.hotdrop.moviememory.di

import dagger.Subcomponent
import jp.hotdrop.moviememory.presentation.movie.MoviesFragment
import jp.hotdrop.moviememory.presentation.movie.comingsoon.ComingSoonMoviesFragment
import jp.hotdrop.moviememory.presentation.movie.nowplaying.NowPlayingMoviesFragment

@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {
    fun inject(fragment: MoviesFragment)
    fun inject(fragment: NowPlayingMoviesFragment)
    fun inject(fragment: ComingSoonMoviesFragment)
}