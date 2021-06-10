package com.alexandrosbentevis.data.datasources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alexandrosbentevis.data.datasources.local.converters.StringListConverter
import com.alexandrosbentevis.data.datasources.local.dao.BeerDao
import com.alexandrosbentevis.data.datasources.local.models.BeerEntity

/**
 * The database of the beers.
 */
@Database(
    entities = [BeerEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class BeerDatabase  : RoomDatabase(){

    abstract fun getBeerDao() : BeerDao

    companion object {
        const val DATABASE_NAME: String = "beerdatabase"
    }
}