package com.alexandrosbentevis.beer.di

import com.alexandrosbentevis.domain.repositories.BeerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import com.alexandrosbentevis.domain.usecases.GetAllBeersUseCase
import com.alexandrosbentevis.domain.usecases.GetBeerUseCase

/**
 * Provides the use cases to the view models.
 */
@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideGetAllBeersUseCase(beerRepository: BeerRepository): GetAllBeersUseCase =
        GetAllBeersUseCase(beerRepository)

    @Provides
    @ViewModelScoped
    fun provideGetBeerUseCase(beerRepository: BeerRepository): GetBeerUseCase =
        GetBeerUseCase(beerRepository)
}