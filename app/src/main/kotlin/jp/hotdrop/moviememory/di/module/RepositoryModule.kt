package jp.hotdrop.moviememory.di.module

import dagger.Binds
import dagger.Module
import jp.hotdrop.moviememory.data.repository.MovieDataRepository
import jp.hotdrop.moviememory.data.repository.MovieRepository

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindMovieRepository(repository: MovieDataRepository): MovieRepository
}