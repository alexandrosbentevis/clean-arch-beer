package com.alexandrosbentevis.data.datasources.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson

/**
 * Database converter for a list of strings.
 */
class StringListConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromString(stringListString: String): List<String> =
        gson.fromJson(stringListString, Array<String>::class.java).toList()

    @TypeConverter
    fun toString(stringList: List<String>): String = gson.toJson(stringList)
}