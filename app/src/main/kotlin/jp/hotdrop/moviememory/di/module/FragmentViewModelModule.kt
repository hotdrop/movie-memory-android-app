package jp.hotdrop.moviememory.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import jp.hotdrop.moviememory.di.ViewModelKey
import jp.hotdrop.moviememory.presentation.movie.comingsoon.ComingSoonMoviesViewModel
import jp.hotdrop.moviememory.presentation.movie.nowplaying.NowPlayingMoviesViewModel
import jp.hotdrop.moviememory.presentation.movie.past.PastMoviesViewModel

@Module
abstract class FragmentViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NowPlayingMoviesViewModel::class)
    abstract fun bindNowPlayingMoviesViewModel(viewModel: NowPlayingMoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ComingSoonMoviesViewModel::class)
    abstract fun bindComingSoonMoviesViewModel(viewModel: ComingSoonMoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PastMoviesViewModel::class)
    abstract fun bindPastMoviesViewModel(viewModel: PastMoviesViewModel): ViewModel
}