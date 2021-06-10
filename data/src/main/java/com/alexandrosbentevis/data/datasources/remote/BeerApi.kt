package com.alexandrosbentevis.data.datasources.remote

import com.alexandrosbentevis.data.datasources.remote.models.BeerDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * The api data source.
 */
interface BeerApi {

    /**
     * Gets a list of beers from the api.
     *
     * @return a list of beers
     */
    @GET("v2/beers")
    suspend fun getAllBeers(): List<BeerDto>

    /**
     * Gets a beer by its id from the api.
     * @param id the id of the beer
     *
     * @return a list of beers
     */
    @GET("v2/beers/{id}")
    suspend fun getBeerById(@Path("id") id: String): List<BeerDto> // Punk api returns the response as a list
}