package jp.hotdrop.moviememory.di.component

import dagger.Subcomponent
import jp.hotdrop.moviememory.di.module.FragmentModule
import jp.hotdrop.moviememory.di.module.FragmentViewModelModule
import jp.hotdrop.moviememory.presentation.movie.MoviesFragment
import jp.hotdrop.moviememory.presentation.movie.comingsoon.ComingSoonMoviesFragment
import jp.hotdrop.moviememory.presentation.movie.detail.MovieDetailEditFragment
import jp.hotdrop.moviememory.presentation.movie.detail.MovieDetailFragment
import jp.hotdrop.moviememory.presentation.movie.nowplaying.NowPlayingMoviesFragment

@Subcomponent(modules = [
    FragmentModule::class,
    FragmentViewModelModule::class
])
interface FragmentComponent {
    fun inject(fragment: MoviesFragment)
    fun inject(fragment: NowPlayingMoviesFragment)
    fun inject(fragment: ComingSoonMoviesFragment)
    fun inject(fragment: MovieDetailFragment)
    fun inject(fragment: MovieDetailEditFragment)
}