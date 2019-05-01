package jp.hotdrop.moviememory.di.component

import dagger.Subcomponent
import jp.hotdrop.moviememory.di.module.ActivityViewModelModule
import jp.hotdrop.moviememory.presentation.MainActivity
import jp.hotdrop.moviememory.presentation.movie.detail.CategoryAttachActivity
import jp.hotdrop.moviememory.presentation.movie.detail.MovieDetailActivity
import jp.hotdrop.moviememory.presentation.movie.edit.MovieEditActivity
import jp.hotdrop.moviememory.presentation.search.SearchResultActivity

@Subcomponent(modules = [
    ActivityViewModelModule::class
])
interface ActivityComponent {

    fun inject(activity: MainActivity)
    fun inject(activity: MovieDetailActivity)
    fun inject(activity: MovieEditActivity)
    fun inject(activity: CategoryAttachActivity)
    fun inject(activity: SearchResultActivity)

    fun fragment(): FragmentComponent
}