package com.alexandrosbentevis.domain.usecases

import com.alexandrosbentevis.domain.framework.Result
import com.alexandrosbentevis.domain.models.Beer
import com.alexandrosbentevis.domain.repositories.BeerRepository
import kotlinx.coroutines.flow.Flow

/**
 * The use case of getting a beer.
 *
 * @property beerRepository The repository used to fetch the beer.
 */
class GetBeerUseCase(val beerRepository: BeerRepository) {

    /**
     * The method to invoke the use case.
     * @param params The params of the use case.
     *
     * @returns a beer wrapped in a [Result] as a [Flow].
     */
    suspend operator fun invoke(params: Params): Flow<Result<Beer>> {
        return beerRepository.getBeerById(id = params.id, forceRefresh = params.forceRefresh)
    }

    /**
     * The params of the use case.
     *
     * @property forceRefresh flag for forcing a refresh of data.
     * @property id The id of the beer.
     */
    data class Params (
        val forceRefresh: Boolean = false,
        val id: String
    )
}

