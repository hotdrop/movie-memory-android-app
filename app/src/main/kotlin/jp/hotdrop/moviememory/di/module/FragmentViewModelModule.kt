package jp.hotdrop.moviememory.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import jp.hotdrop.moviememory.di.ViewModelKey
import jp.hotdrop.moviememory.presentation.category.CategoryViewModel
import jp.hotdrop.moviememory.presentation.movie.MoviesViewModel
import jp.hotdrop.moviememory.presentation.movie.tab.TabMoviesViewModel
import jp.hotdrop.moviememory.presentation.search.SearchViewModel
import jp.hotdrop.moviememory.presentation.setting.SettingViewModel

@Module
abstract class FragmentViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun bindMoviesViewModel(viewModel: MoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TabMoviesViewModel::class)
    abstract fun bindTabMoviesViewModel(viewModel: TabMoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun bindCategoryViewModel(viewModel: CategoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingViewModel::class)
    abstract fun bindSettingViewModel(viewModel: SettingViewModel): ViewModel
}