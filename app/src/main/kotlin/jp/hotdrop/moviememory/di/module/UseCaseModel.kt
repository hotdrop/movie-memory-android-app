package jp.hotdrop.moviememory.di.module

import dagger.Binds
import dagger.Module
import jp.hotdrop.moviememory.domain.MoviesUseCase

@Module
abstract class UseCaseModel {

    @Binds
    abstract fun bindMoviesUseCase(useCase: MoviesUseCase): MoviesUseCase
}