package com.alexandrosbentevis.data.datasources.local.dao

import androidx.room.*
import com.alexandrosbentevis.data.datasources.local.models.BeerEntity

/**
 * Classes where database interactions are defined.
 */
@Dao
interface BeerDao {
    /**
     * Gets all beers from the database.
     *
     * @return A list of all the beers in the database.
     */
    @Query("SELECT * FROM beers WHERE name LIKE '%' || :filterByName || '%' ORDER BY name ASC")
    suspend fun getAllBeers(filterByName: String = "" ): List<BeerEntity>

    /**
     * Adds a list of beers to the database. If a beers exists, it replaces it.
     * @param beers the list of beers to be added.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBeers(beers: List<BeerEntity>)

    /**
     * Adds a beer to the database. If the beers exists, it replaces it.
     * @param beer the beer to be added.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBeer(beer: BeerEntity)

    /**
     * Gets a beer by its id.
     * @param id the id of the beer.
     *
     * @return The beer from database.
     */
    @Query("SELECT * FROM beers WHERE id = :id")
    suspend fun getBeerById(id: String) : BeerEntity

    /**
     * Deletes everything from the table
     */
    @Query("DELETE FROM beers")
    suspend fun deleteAll()

    /**
     * Replaces all beers in the database.
     * @param beers the new beers to be added.
     */
    @Transaction
    suspend fun addAllBeers(beers: List<BeerEntity>) {
        deleteAll()
        addBeers(beers)
    }
}