package jp.hotdrop.moviememory.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import jp.hotdrop.moviememory.di.ViewModelKey
import jp.hotdrop.moviememory.presentation.movie.detail.MovieDetailEditViewModel
import jp.hotdrop.moviememory.presentation.movie.detail.MovieDetailViewModel
import jp.hotdrop.moviememory.presentation.movie.nowplaying.NowPlayingMoviesViewModel

@Module
abstract class FragmentViewModelModule {

    @Binds @IntoMap @ViewModelKey(NowPlayingMoviesViewModel::class)
    abstract fun bindNowPlayingMoviesViewModel(viewModel: NowPlayingMoviesViewModel): ViewModel

    @Binds @IntoMap @ViewModelKey(MovieDetailViewModel::class)
    abstract fun bindMovieDetailViewModel(viewModel: MovieDetailViewModel): ViewModel

    @Binds @IntoMap @ViewModelKey(MovieDetailEditViewModel::class)
    abstract fun bindMovieDetailEditViewModel(viewModel: MovieDetailEditViewModel): ViewModel
}