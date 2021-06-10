package com.alexandrosbentevis.beer.di

import android.content.Context
import androidx.room.Room
import com.alexandrosbentevis.data.datasources.local.BeerDatabase
import com.alexandrosbentevis.data.datasources.local.dao.BeerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides the database and the dao.
 */
@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideBeerDatabase(@ApplicationContext context: Context): BeerDatabase {
        return Room.databaseBuilder(
            context,
            BeerDatabase::class.java,
            BeerDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideBeerDao(database: BeerDatabase): BeerDao {
        return database.getBeerDao()
    }
}