package com.alexandrosbentevis.beer.di

import android.content.Context
import androidx.room.Room
import com.alexandrosbentevis.data.datasources.local.BeerDatabase
import com.alexandrosbentevis.data.datasources.local.dao.BeerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
class TestDatabaseModule {

    @Singleton
    @Provides
    fun provideBeerDatabase(@ApplicationContext context: Context): BeerDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            BeerDatabase::class.java,
        ).allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun provideBeerDao(database: BeerDatabase): BeerDao {
        return database.getBeerDao()
    }
}