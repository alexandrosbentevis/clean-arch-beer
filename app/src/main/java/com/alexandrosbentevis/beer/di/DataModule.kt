package com.alexandrosbentevis.beer.di

import com.alexandrosbentevis.data.datasources.local.dao.BeerDao
import com.alexandrosbentevis.data.datasources.remote.BeerApi
import com.alexandrosbentevis.data.repositories.BeerRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Provides the repositories.
 */
@Module
@InstallIn(FragmentComponent::class)
class DataModule {

    @Provides
    @FragmentScoped
    fun provideBeerRepositoryImpl(
        dispatcher: CoroutineDispatcher,
        beerApi: BeerApi,
        beerDao: BeerDao
    ) = BeerRepositoryImpl(dispatcher = dispatcher, beerApi = beerApi, beerDao = beerDao)
}