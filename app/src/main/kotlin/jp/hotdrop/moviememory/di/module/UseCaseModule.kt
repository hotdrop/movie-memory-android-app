package jp.hotdrop.moviememory.di.module

import dagger.Module
import dagger.Provides
import jp.hotdrop.moviememory.data.repository.MovieRepository
import jp.hotdrop.moviememory.domain.MovieUseCase
import javax.inject.Singleton

@Module
class UseCaseModule {

    @Provides
    @Singleton
    fun provideMovieUseCase(repository: MovieRepository) = MovieUseCase(repository)
}