package com.alexandrosbentevis.data.repositories

import app.cash.turbine.test
import com.alexandrosbentevis.data.datasources.local.dao.BeerDao
import com.alexandrosbentevis.data.datasources.local.models.mapToEntity
import com.alexandrosbentevis.data.datasources.remote.BeerApi
import com.alexandrosbentevis.data.datasources.remote.models.mapToDomain
import com.alexandrosbentevis.data.factory.BeerDtoFactory
import com.alexandrosbentevis.data.factory.BeerEntityFactory
import com.alexandrosbentevis.data.framework.CoroutinesTestRule
import com.alexandrosbentevis.domain.framework.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.`should be instance of`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.time.ExperimentalTime
import com.alexandrosbentevis.data.datasources.local.models.mapToDomain
import com.nhaarman.mockitokotlin2.*
import com.nhaarman.mockitokotlin2.doReturn
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import java.net.ConnectException
import java.net.UnknownHostException

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@ExperimentalTime
@RunWith(JUnit4::class)
class BeerRepositoryImplTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private lateinit var beerRepository: BeerRepositoryImpl
    private lateinit var beerDao: BeerDao
    private lateinit var beerApi: BeerApi

    @Test
    fun `Given we have entities in the database, when getBeers() is called, then the database and the api should be used`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {

            // GIVEN
            val beerEntityListFromDatabase = BeerEntityFactory.createBeerEntityList(count = 5)
            val beerDtoListFromApi = BeerDtoFactory.createBeerDtoList(count = 10)

            val dataToBeSaved = beerDtoListFromApi.map {it.mapToDomain() }.map{ it.mapToEntity() }

            beerApi = mock {
                onBlocking { getAllBeers() } doReturn beerDtoListFromApi
            }

            beerDao = mock {
                onBlocking { getAllBeers() } doReturn beerEntityListFromDatabase
                onBlocking { addAllBeers(any()) } doReturn Unit
            }

            beerRepository = BeerRepositoryImpl(
                dispatcher = coroutinesTestRule.testDispatcher,
                beerApi = beerApi,
                beerDao = beerDao
            )

            // WHEN
            beerRepository.getBeers(filterByName = "").test {
                expectItem()
                expectComplete()
            }

            // THEN
            verify(beerDao, times(2)).getAllBeers()
            verify(beerDao).addAllBeers(dataToBeSaved)
            verify(beerApi).getAllBeers()
        }
    }

    @Test
    fun `Given we have entities in the database, when getBeers() is called with flag forceRefresh, then the first database fetch should be skipped`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {

            // GIVEN
            val beerEntityListFromDatabase = BeerEntityFactory.createBeerEntityList(count = 5)
            val beerDtoListFromApi = BeerDtoFactory.createBeerDtoList(count = 10)

            val dataToBeSaved = beerDtoListFromApi.map {it.mapToDomain() }.map{ it.mapToEntity() }

            beerApi = mock {
                onBlocking { getAllBeers() } doReturn beerDtoListFromApi
            }

            beerDao = mock {
                onBlocking { getAllBeers() } doReturn beerEntityListFromDatabase
                onBlocking { addAllBeers(any()) } doReturn Unit
            }

            beerRepository = BeerRepositoryImpl(
                dispatcher = coroutinesTestRule.testDispatcher,
                beerApi = beerApi,
                beerDao = beerDao
            )

            // WHEN
            beerRepository.getBeers(forceRefresh = true, filterByName = "").test {
                expectItem()
                expectComplete()
            }

            // THEN
            verify(beerDao, times(1)).getAllBeers()
            verify(beerDao).addAllBeers(dataToBeSaved)
            verify(beerApi).getAllBeers()
        }
    }

    @Test
    fun `Given we have entities in the database, when getBeers() is called, then it should use the database to fetch data first and then the api`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {

            // GIVEN
            val beerEntityListFromDatabase = BeerEntityFactory.createBeerEntityList(count = 5)
            val beerDtoListFromApi = BeerDtoFactory.createBeerDtoList(count = 10)
            val beerEntityListFromDatabaseFresh = beerDtoListFromApi.map{ it.mapToDomain() }.map{ it.mapToEntity() }

            beerApi = mock {
                onBlocking { getAllBeers() } doReturn beerDtoListFromApi
            }

            beerDao = mock {
                onBlocking { getAllBeers(any()) } doReturnConsecutively listOf(beerEntityListFromDatabase, beerEntityListFromDatabaseFresh)
                onBlocking { addAllBeers(any()) } doReturn Unit
            }

            beerRepository = BeerRepositoryImpl(
                dispatcher = coroutinesTestRule.testDispatcher,
                beerApi = beerApi,
                beerDao = beerDao
            )

            // WHEN
            beerRepository.getBeers(filterByName = "").test {
                // THEN
                expectItem().apply {
                    this `should be instance of` Result.Success::class.java
                    (this as Result.Success).data `should equal` beerEntityListFromDatabase.map{ it.mapToDomain() }
                }
                expectItem().apply {
                    this `should be instance of` Result.Success::class.java
                    (this as Result.Success).data `should equal` beerDtoListFromApi.map{ it.mapToDomain() }
                }
                expectComplete()
            }
        }
    }

    @Test
    fun `Given we have entities in the database, when getBeers() is called with flag forceRefresh, then it should use only the api`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {

            // GIVEN
            val beerDtoListFromApi = BeerDtoFactory.createBeerDtoList(count = 10)
            val beerEntityListFromDatabaseFresh = beerDtoListFromApi.map{ it.mapToDomain() }.map{ it.mapToEntity() }

            beerApi = mock {
                onBlocking { getAllBeers() } doReturn beerDtoListFromApi
            }

            beerDao = mock {
                onBlocking { getAllBeers(any()) } doReturn  beerEntityListFromDatabaseFresh
                onBlocking { addAllBeers(any()) } doReturn Unit
            }

            beerRepository = BeerRepositoryImpl(
                dispatcher = coroutinesTestRule.testDispatcher,
                beerApi = beerApi,
                beerDao = beerDao
            )

            // WHEN
            beerRepository.getBeers(forceRefresh = true, filterByName = "").test {
                // THEN
                expectItem().apply {
                    this `should be instance of` Result.Success::class.java
                    (this as Result.Success).data `should equal` beerDtoListFromApi.map{ it.mapToDomain() }
                }
                expectComplete()
            }
        }
    }

    @Test
    fun `Given we have a non-empty filter query string, when getBeers() is called, then the data should be filtered by name`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {

            // GIVEN
            val beerEntityListFromDatabase = BeerEntityFactory.createBeerEntityList(count = 5)
            val beerDtoListFromApi = BeerDtoFactory.createBeerDtoList(count = 5)
            val filter = "Beer 1"

            beerApi = mock {
                onBlocking { getAllBeers() } doReturn beerDtoListFromApi
            }

            beerDao = mock {
                onBlocking { getAllBeers(filter) } doReturn listOf(beerEntityListFromDatabase[1])
                onBlocking { addAllBeers(any()) } doReturn Unit
            }

            beerRepository = BeerRepositoryImpl(
                dispatcher = coroutinesTestRule.testDispatcher,
                beerApi = beerApi,
                beerDao = beerDao
            )

            // WHEN
            beerRepository.getBeers(filterByName = filter).test {
                // THEN
                expectItem().apply {
                    this `should be instance of` Result.Success::class.java
                    (this as Result.Success).data `should equal` listOf(beerEntityListFromDatabase[1]).map{ it.mapToDomain() }
                }
                expectComplete()
            }
        }
    }

    @Test
    fun `Given the database and the api return the same data , when getBeers() is called, then it should use just return the data of the database`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {

            // GIVEN
            val beerEntityListFromDatabase = BeerEntityFactory.createBeerEntityList(count = 5)
            val beerDtoListFromApi = BeerDtoFactory.createBeerDtoList(count = 5)

            beerApi = mock {
                onBlocking { getAllBeers() } doReturn beerDtoListFromApi
            }

            beerDao = mock {
                onBlocking { getAllBeers(any()) } doReturn beerEntityListFromDatabase
                onBlocking { addAllBeers(any()) } doReturn Unit
            }

            beerRepository = BeerRepositoryImpl(
                dispatcher = coroutinesTestRule.testDispatcher,
                beerApi = beerApi,
                beerDao = beerDao
            )

            // WHEN
            beerRepository.getBeers(filterByName = "").test {
                // THEN
                expectItem().apply {
                    this `should be instance of` Result.Success::class.java
                    (this as Result.Success).data `should equal` beerEntityListFromDatabase.map{ it.mapToDomain() }
                }
                expectComplete()
            }
        }
    }


    @Test
    fun `Given we have entities in the database and the api is throwing an UnknownHostException error, when getBeers() is called, then it should only emit the cached data`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {

            // GIVEN
            val exception = UnknownHostException("Exception")
            val beerEntityListFromDatabase = BeerEntityFactory.createBeerEntityList(count = 5)

            beerApi = mock {
                onBlocking { getAllBeers() } doAnswer { throw exception }
            }

            beerDao = mock {
                onBlocking { getAllBeers(any()) } doReturn beerEntityListFromDatabase
                onBlocking { addAllBeers(any()) } doReturn Unit
            }

            beerRepository = BeerRepositoryImpl(
                dispatcher = coroutinesTestRule.testDispatcher,
                beerApi = beerApi,
                beerDao = beerDao
            )

            // WHEN
            beerRepository.getBeers(filterByName = "").test {
                // THEN
                expectItem().apply {
                    this `should be instance of` Result.Success::class.java
                    (this as Result.Success).data `should equal` beerEntityListFromDatabase.map{ it.mapToDomain() }
                }
                expectComplete()
            }
        }
    }

    @Test
    fun `Given we have entities in the database and the api is throwing an ConnectException error, when getBeers() is called, then it should only emit the cached data`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {

            // GIVEN
            val exception = ConnectException("Exception")
            val beerEntityListFromDatabase = BeerEntityFactory.createBeerEntityList(count = 5)

            beerApi = mock {
                onBlocking { getAllBeers() } doAnswer { throw exception }
            }

            beerDao = mock {
                onBlocking { getAllBeers(any()) } doReturn beerEntityListFromDatabase
                onBlocking { addAllBeers(any()) } doReturn Unit
            }

            beerRepository = BeerRepositoryImpl(
                dispatcher = coroutinesTestRule.testDispatcher,
                beerApi = beerApi,
                beerDao = beerDao
            )

            // WHEN
            beerRepository.getBeers(filterByName = "").test {
                // THEN
                expectItem().apply {
                    this `should be instance of` Result.Success::class.java
                    (this as Result.Success).data `should equal` beerEntityListFromDatabase.map{ it.mapToDomain() }
                }
                expectComplete()
            }
        }
    }

    @Test
    fun `Given we have entities in the database and the api is throwing any other error, when getBeers() is called, then we should emit the cached data first and then emit the error`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {

            // GIVEN
            val exception = RuntimeException("Exception")
            val beerEntityListFromDatabase = BeerEntityFactory.createBeerEntityList(count = 5)

            beerApi = mock {
                onBlocking { getAllBeers() } doAnswer { throw exception }
            }

            beerDao = mock {
                onBlocking { getAllBeers(any()) } doReturn beerEntityListFromDatabase
                onBlocking { addAllBeers(any()) } doReturn Unit
            }

            beerRepository = BeerRepositoryImpl(
                dispatcher = coroutinesTestRule.testDispatcher,
                beerApi = beerApi,
                beerDao = beerDao
            )

            // WHEN
            beerRepository.getBeers(filterByName = "").test {
                // THEN
                expectItem().apply {
                    this `should be instance of` Result.Success::class.java
                    (this as Result.Success).data `should equal` beerEntityListFromDatabase.map{ it.mapToDomain() }
                }
                expectItem().apply {
                    this `should be instance of` Result.Error::class.java
                    (this as Result.Error).exc `should be` exception
                }
                expectComplete()
            }
        }
    }

    @Test
    fun `Given we have entities in the database, when getBeerById() is called, then the database and the api should be used`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {

            // GIVEN
            val id = "5"
            val beerEntityFromDatabase = BeerEntityFactory.createBeerEntity(id = id.toInt())
            val beerDtoListFromApi = BeerDtoFactory.createBeerDtoList(count = 1)

            val dataToBeSaved = beerDtoListFromApi.map {it.mapToDomain() }.map{ it.mapToEntity() }.first()

            beerApi = mock {
                onBlocking { getBeerById(any()) } doReturn beerDtoListFromApi
            }

            beerDao = mock {
                onBlocking { getBeerById(any()) } doReturn beerEntityFromDatabase
                onBlocking { addBeer(any()) } doReturn Unit
            }

            beerRepository = BeerRepositoryImpl(
                dispatcher = coroutinesTestRule.testDispatcher,
                beerApi = beerApi,
                beerDao = beerDao
            )

            // WHEN
            beerRepository.getBeerById(id = id).test {
                expectItem()
                expectComplete()
            }

            // THEN
            verify(beerDao, times(2)).getBeerById(id)
            verify(beerDao).addBeer(dataToBeSaved)
            verify(beerApi).getBeerById(id)
        }
    }

    @Test
    fun `Given we have entities in the database, when getBeerById() is called with flag forceRefresh, then the first database fetch should be skipped`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {

            // GIVEN
            val id = "5"
            val beerEntityFromDatabase = BeerEntityFactory.createBeerEntity(id = id.toInt())
            val beerDtoListFromApi = BeerDtoFactory.createBeerDtoList(count = 1)

            val dataToBeSaved = beerDtoListFromApi.map {it.mapToDomain() }.map{ it.mapToEntity() }.first()

            beerApi = mock {
                onBlocking { getBeerById(any()) } doReturn beerDtoListFromApi
            }

            beerDao = mock {
                onBlocking { getBeerById(any()) } doReturn beerEntityFromDatabase
                onBlocking { addBeer(any()) } doReturn Unit
            }

            beerRepository = BeerRepositoryImpl(
                dispatcher = coroutinesTestRule.testDispatcher,
                beerApi = beerApi,
                beerDao = beerDao
            )

            // WHEN
            beerRepository.getBeerById(forceRefresh = true, id = id).test {
                expectItem()
                expectComplete()
            }

            // THEN
            verify(beerDao, times(1)).getBeerById(id)
            verify(beerDao).addBeer(dataToBeSaved)
            verify(beerApi).getBeerById(id)
        }
    }

    @Test
    fun `Given we have entities in the database, when getBeerById() is called, then it should use the database to fetch data first and then fresh from the api`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {

            // GIVEN
            val id = "5"
            val beerEntityFromDatabase = BeerEntityFactory.createBeerEntity(id = id.toInt())
            val beerDtoListFromApi = BeerDtoFactory.createBeerDtoList(count = 1)
            val beerEntityFromDatabaseFresh = beerDtoListFromApi.first().mapToDomain().mapToEntity()

            beerApi = mock {
                onBlocking { getBeerById(any()) } doReturn beerDtoListFromApi
            }

            beerDao = mock {
                onBlocking { getBeerById(any()) } doReturnConsecutively listOf(beerEntityFromDatabase, beerEntityFromDatabaseFresh)
                onBlocking { addBeer(any()) } doReturn Unit
            }

            beerRepository = BeerRepositoryImpl(
                dispatcher = coroutinesTestRule.testDispatcher,
                beerApi = beerApi,
                beerDao = beerDao
            )

            // WHEN
            beerRepository.getBeerById(id = id).test {
                expectItem().apply {
                    this `should be instance of` Result.Success::class.java
                    (this as Result.Success).data `should equal` beerEntityFromDatabase.mapToDomain()
                }
                expectItem().apply {
                    this `should be instance of` Result.Success::class.java
                    (this as Result.Success).data `should equal` beerEntityFromDatabaseFresh.mapToDomain()
               }
                expectComplete()
            }
        }
    }

    @Test
    fun `Given we have entities in the database, when getBeerById() is called with flag forceRefresh, then it should use only the api`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {

            // GIVEN
            val id = "5"
            val beerDtoListFromApi = BeerDtoFactory.createBeerDtoList(count = 1)
            val beerEntityFromDatabaseFresh = beerDtoListFromApi.first().mapToDomain().mapToEntity()

            beerApi = mock {
                onBlocking { getBeerById(any()) } doReturn beerDtoListFromApi
            }

            beerDao = mock {
                onBlocking { getBeerById(any()) } doReturn beerEntityFromDatabaseFresh
                onBlocking { addBeer(any()) } doReturn Unit
            }

            beerRepository = BeerRepositoryImpl(
                dispatcher = coroutinesTestRule.testDispatcher,
                beerApi = beerApi,
                beerDao = beerDao
            )

            // WHEN
            beerRepository.getBeerById(forceRefresh = true, id = id).test {
                expectItem().apply {
                    this `should be instance of` Result.Success::class.java
                    (this as Result.Success).data `should equal` beerEntityFromDatabaseFresh.mapToDomain()
                }
                expectComplete()
            }
        }
    }

    @Test
    fun `Given the database and the api return the same data , when getBeersById() is called, then it should use just return the data of the database`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {

            // GIVEN
            val id = "0"
            val beerEntityFromDatabase = BeerEntityFactory.createBeerEntity(id = id.toInt())
            val beerDtoListFromApi = listOf(BeerDtoFactory.createBeerDto(id = id.toInt()))

            beerApi = mock {
                onBlocking { getBeerById(any()) } doReturn beerDtoListFromApi
            }

            beerDao = mock {
                onBlocking { getBeerById(any()) } doReturn beerEntityFromDatabase
                onBlocking { addBeer(any()) } doReturn Unit
            }

            beerRepository = BeerRepositoryImpl(
                dispatcher = coroutinesTestRule.testDispatcher,
                beerApi = beerApi,
                beerDao = beerDao
            )

            // WHEN
            beerRepository.getBeerById(id = id).test {
                expectItem().apply {
                    this `should be instance of` Result.Success::class.java
                    (this as Result.Success).data `should equal` beerEntityFromDatabase.mapToDomain()
                }
                expectComplete()
            }
        }
    }

    @Test
    fun `Given we have entities in the database and the api is throwing an UnknownHostException error, when getBeerById() is called, then it should only emit the cached data`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {

            // GIVEN
            val id = "0"
            val exception = UnknownHostException("Exception")
            val beerEntityFromDatabase = BeerEntityFactory.createBeerEntity(id = id.toInt())

            beerApi = mock {
                onBlocking { getBeerById(any()) } doAnswer { throw exception }
            }

            beerDao = mock {
                onBlocking { getBeerById(any()) } doReturn beerEntityFromDatabase
                onBlocking { addBeer(any()) } doReturn Unit
            }

            beerRepository = BeerRepositoryImpl(
                dispatcher = coroutinesTestRule.testDispatcher,
                beerApi = beerApi,
                beerDao = beerDao
            )

            // WHEN
            beerRepository.getBeerById(id = id).test {
                expectItem().apply {
                    this `should be instance of` Result.Success::class.java
                    (this as Result.Success).data `should equal` beerEntityFromDatabase.mapToDomain()
                }
                expectComplete()
            }
        }
    }

    @Test
    fun `Given we have entities in the database and the api is throwing an ConnectException error, when getBeerById() is called, then it should only emit the cached data`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {

            // GIVEN
            val id = "0"
            val exception = ConnectException("Exception")
            val beerEntityFromDatabase = BeerEntityFactory.createBeerEntity(id = id.toInt())

            beerApi = mock {
                onBlocking { getBeerById(any()) } doAnswer { throw exception }
            }

            beerDao = mock {
                onBlocking { getBeerById(any()) } doReturn beerEntityFromDatabase
                onBlocking { addBeer(any()) } doReturn Unit
            }

            beerRepository = BeerRepositoryImpl(
                dispatcher = coroutinesTestRule.testDispatcher,
                beerApi = beerApi,
                beerDao = beerDao
            )

            // WHEN
            beerRepository.getBeerById(id = id).test {
                expectItem().apply {
                    this `should be instance of` Result.Success::class.java
                    (this as Result.Success).data `should equal` beerEntityFromDatabase.mapToDomain()
                }
                expectComplete()
            }
        }
    }

    @Test
    fun `Given we have entities in the database and the api is throwing any other error, when getBeerById() is called, then we should emit the cached data first and then emit the error`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {

            // GIVEN
            val id = "0"
            val exception = RuntimeException("Exception")
            val beerEntityFromDatabase = BeerEntityFactory.createBeerEntity(id = id.toInt())

            beerApi = mock {
                onBlocking { getBeerById(any()) } doAnswer { throw exception }
            }

            beerDao = mock {
                onBlocking { getBeerById(any()) } doReturn beerEntityFromDatabase
                onBlocking { addBeer(any()) } doReturn Unit
            }

            beerRepository = BeerRepositoryImpl(
                dispatcher = coroutinesTestRule.testDispatcher,
                beerApi = beerApi,
                beerDao = beerDao
            )

            // WHEN
            beerRepository.getBeerById(id = id).test {
                expectItem().apply {
                    this `should be instance of` Result.Success::class.java
                    (this as Result.Success).data `should equal` beerEntityFromDatabase.mapToDomain()
                }
                expectItem().apply {
                    this `should be instance of` Result.Error::class.java
                    (this as Result.Error).exc `should be` exception
                }
                expectComplete()
            }
        }
    }
}