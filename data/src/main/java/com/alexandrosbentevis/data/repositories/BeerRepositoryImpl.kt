package com.alexandrosbentevis.data.repositories

import com.alexandrosbentevis.data.datasources.local.dao.BeerDao
import com.alexandrosbentevis.data.datasources.local.models.mapToDomain
import com.alexandrosbentevis.data.datasources.local.models.mapToEntity
import com.alexandrosbentevis.data.datasources.remote.BeerApi
import com.alexandrosbentevis.data.datasources.remote.models.mapToDomain
import com.alexandrosbentevis.data.extensions.emitData
import com.alexandrosbentevis.domain.framework.Result
import com.alexandrosbentevis.domain.models.Beer
import com.alexandrosbentevis.domain.repositories.BeerRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * The implementation of the [BeerRepository].
 *
 * @property dispatcher the coroutine dispatcher.
 * @property beerApi the api data source.
 * @property beerDao the db data source.
 */
class BeerRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val beerApi: BeerApi,
    private val beerDao: BeerDao
) : BeerRepository {

    override suspend fun getBeers(
        forceRefresh: Boolean,
        filterByName: String
    ): Flow<Result<List<Beer>>> = flow {
        emitData(
            forceRefresh = forceRefresh,
            loadFromDb = { beerDao.getAllBeers(filterByName = filterByName).map { it.mapToDomain() } },
            saveToDb = { data -> beerDao.addAllBeers(data.map { it.mapToEntity() }) },
            loadFromApi = { beerApi.getAllBeers().map { it.mapToDomain() } }
        )
    }.flowOn(dispatcher)

    override suspend fun getBeerById(forceRefresh: Boolean, id: String): Flow<Result<Beer>> = flow {
        emitData(
            forceRefresh = forceRefresh,
            loadFromDb = { beerDao.getBeerById(id = id).mapToDomain() },
            saveToDb = { data -> beerDao.addBeer(data.mapToEntity()) },
            loadFromApi = { beerApi.getBeerById(id = id).first().mapToDomain()  }
        )
    }.flowOn(dispatcher)
}