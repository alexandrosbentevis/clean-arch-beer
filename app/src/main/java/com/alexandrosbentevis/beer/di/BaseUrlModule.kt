package com.alexandrosbentevis.beer.di

import com.alexandrosbentevis.beer.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

/**
 * Provides the base url for the api.
 */
@Module
@InstallIn(SingletonComponent::class)
class BaseUrlModule {

    @Provides
    @Singleton
    @Named("baseUrl")
    fun provideBaseUrl() = BuildConfig.ENDPOINT
}