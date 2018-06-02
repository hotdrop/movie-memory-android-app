package jp.hotdrop.moviememory.di.module

import dagger.Module
import dagger.Provides
import jp.hotdrop.moviememory.data.repository.MovieRepository
import jp.hotdrop.moviememory.usecase.MovieUseCase
import javax.inject.Singleton

@Module
class UseCaseModule {

    @Provides
    @Singleton
    fun provideMovieUseCase(repository: MovieRepository) = MovieUseCase(repository)
}