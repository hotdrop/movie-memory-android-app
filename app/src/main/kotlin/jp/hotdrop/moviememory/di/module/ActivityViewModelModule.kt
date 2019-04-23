package jp.hotdrop.moviememory.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import jp.hotdrop.moviememory.di.ViewModelKey
import jp.hotdrop.moviememory.presentation.movie.edit.MovieEditViewModel
import jp.hotdrop.moviememory.presentation.movie.detail.MovieDetailViewModel
import jp.hotdrop.moviememory.presentation.search.SearchResultViewModel

@Module
abstract class ActivityViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchResultViewModel::class)
    abstract fun bindSearchResultViewModel(viewModel: SearchResultViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel::class)
    abstract fun bindMovieDetailViewModel(viewModel: MovieDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieEditViewModel::class)
    abstract fun bindMovieEditViewModel(viewModel: MovieEditViewModel): ViewModel
}