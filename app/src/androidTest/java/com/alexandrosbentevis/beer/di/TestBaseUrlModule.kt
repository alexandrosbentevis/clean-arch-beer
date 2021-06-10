package com.alexandrosbentevis.beer.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [BaseUrlModule::class]
)
class TestBaseUrlModule {

    @Provides
    @Singleton
    @Named("baseUrl")
    fun provideBaseUrl() = "http://localhost:12345/"
}