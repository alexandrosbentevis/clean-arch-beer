package com.alexandrosbentevis.domain.repositories

import com.alexandrosbentevis.domain.framework.Result
import com.alexandrosbentevis.domain.models.Beer
import kotlinx.coroutines.flow.Flow

/**
 * The repository associated with beers.
 */
interface BeerRepository {

    /**
     * Gets a list of beers using the data sources.
     *
     * @param forceRefresh flag for forcing a refresh of data.
     * @param filterByName Query string to filter the results by name. Empty filter does
     * not filter the results.
     *
     * @returns a list of beers wrapped in a [Result] as a [Flow].
     */
    suspend fun getBeers(forceRefresh: Boolean = false, filterByName: String): Flow<Result<List<Beer>>>

    /**
     * Gets a beer by its id using the data sources.
     *
     * @param forceRefresh flag for forcing a refresh of data.
     * @param id The id of the  of the beer.
     *
     * @returns a beer wrapped in a [Result] as a [Flow].
     */
    suspend fun getBeerById(forceRefresh: Boolean = false, id: String): Flow<Result<Beer>>
}

