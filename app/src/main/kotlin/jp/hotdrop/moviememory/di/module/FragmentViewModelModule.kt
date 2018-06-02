package jp.hotdrop.moviememory.di.module

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import jp.hotdrop.moviememory.di.ViewModelKey
import jp.hotdrop.moviememory.presentation.movie.nowplaying.NowPlayingMoviesViewModel

@Module
abstract class FragmentViewModelModule {

    @Binds @IntoMap @ViewModelKey(NowPlayingMoviesViewModel::class)
    abstract fun bindNowPlayingMoviesViewModel(viewModel: NowPlayingMoviesViewModel): ViewModel
}