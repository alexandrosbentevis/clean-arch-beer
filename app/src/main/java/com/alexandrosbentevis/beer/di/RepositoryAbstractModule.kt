package com.alexandrosbentevis.beer.di

import com.alexandrosbentevis.data.repositories.BeerRepositoryImpl
import com.alexandrosbentevis.domain.repositories.BeerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Binds the repositories to their implementations.
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryAbstractModule {

    @Binds
    abstract fun bindBeerRepository(beerRepositoryImpl: BeerRepositoryImpl): BeerRepository
}