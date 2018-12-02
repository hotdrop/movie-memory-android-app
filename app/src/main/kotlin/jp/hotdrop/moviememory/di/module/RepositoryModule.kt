package jp.hotdrop.moviememory.di.module

import dagger.Binds
import dagger.Module
import jp.hotdrop.moviememory.data.repository.MovieDataRepository
import jp.hotdrop.moviememory.data.repository.MovieRepository
import jp.hotdrop.moviememory.data.repository.SearchDataRepository
import jp.hotdrop.moviememory.data.repository.SearchRepository

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindMovieRepository(repository: MovieDataRepository): MovieRepository

    @Binds
    abstract fun bindSearchRepository(repository: SearchDataRepository): SearchRepository
}