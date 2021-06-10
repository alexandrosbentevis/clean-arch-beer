package com.alexandrosbentevis.beer.di

import com.alexandrosbentevis.beer.framework.NetworkStatusProvider
import com.alexandrosbentevis.beer.framework.NetworkStatusProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds the network status provider interface to its implementation.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkStatusProviderAbstractModule {

    @Singleton
    @Binds
    abstract fun bindNetworkStatusProvider(networkStatusProviderImpl: NetworkStatusProviderImpl): NetworkStatusProvider
}