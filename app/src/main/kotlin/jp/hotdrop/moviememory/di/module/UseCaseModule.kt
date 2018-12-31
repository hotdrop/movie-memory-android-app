package jp.hotdrop.moviememory.di.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import jp.hotdrop.moviememory.data.repository.MovieDataRepository
import jp.hotdrop.moviememory.data.repository.MovieRepository
import jp.hotdrop.moviememory.data.repository.SearchRepository
import jp.hotdrop.moviememory.usecase.MovieUseCase
import jp.hotdrop.moviememory.usecase.SearchUseCase
import jp.hotdrop.moviememory.usecase.UseCase
import javax.inject.Singleton

@Module
abstract class UseCaseModule {

    @Binds
    abstract fun bindMovieUseCase(useCase: MovieUseCase): UseCase

    @Binds
    abstract fun bindSearchUseCase(useCase: SearchUseCase): UseCase
}