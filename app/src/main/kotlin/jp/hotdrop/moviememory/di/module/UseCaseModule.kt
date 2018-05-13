package jp.hotdrop.moviememory.di.module

import dagger.Module
import dagger.Provides
import jp.hotdrop.moviememory.data.repository.MovieRepository
import jp.hotdrop.moviememory.domain.MoviesUseCase
import javax.inject.Singleton

@Module
class UseCaseModule {

    @Provides
    @Singleton
    fun provideMoviesUseCase(repository: MovieRepository) = MoviesUseCase(repository)
}