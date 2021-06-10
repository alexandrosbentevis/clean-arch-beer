package com.alexandrosbentevis.domain.usecases

import com.alexandrosbentevis.domain.framework.Result
import com.alexandrosbentevis.domain.models.Beer
import com.alexandrosbentevis.domain.repositories.BeerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * The use case of getting a list of beers filtered by the name of the beer.
 *
 * @property beerRepository The repository used to fetch the beers.
 */
class GetAllBeersUseCase @Inject constructor(
    private val beerRepository: BeerRepository
) {
    /**
     * The method to invoke the use case.
     * @param params The params of the use case.
     *
     * @returns a list of beers wrapped in a [Result] as a [Flow].
     */
    suspend operator fun invoke(params: Params): Flow<Result<List<Beer>>> {
        return beerRepository.getBeers(filterByName = params.filterByName, forceRefresh = params.forceRefresh)
    }

    /**
     * The params of the use case.
     *
     * @property forceRefresh flag for forcing a refresh of data.
     * @property filterByName Query string for filtering the results by name.
     */
    data class Params (
        val forceRefresh: Boolean = false,
        val filterByName: String
    )
}

