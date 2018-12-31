package jp.hotdrop.moviememory.di.module

import dagger.Binds
import dagger.Module
import jp.hotdrop.moviememory.data.repository.*

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindMovieRepository(repository: MovieDataRepository): MovieRepository

    @Binds
    abstract fun bindCategoryRepository(repository: CategoryDataRepository): CategoryRepository

    @Binds
    abstract fun bindSearchRepository(repository: SearchDataRepository): SearchRepository
}