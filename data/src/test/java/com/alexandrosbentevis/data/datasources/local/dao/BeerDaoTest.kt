package com.alexandrosbentevis.data.datasources.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.alexandrosbentevis.data.datasources.local.BeerDatabase
import com.alexandrosbentevis.data.factory.BeerEntityFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.`should equal`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@Config(manifest=Config.NONE, sdk = [29])
@RunWith(RobolectricTestRunner::class)
class BeerDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: BeerDatabase

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BeerDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Test
    fun `Given the db table is empty, when getAllBeers() is called, then it should return an empty list`() = runBlockingTest {

        // WHEN
        val beerList = db.getBeerDao().getAllBeers()

        // THEN
        beerList.size `should equal` 0
    }

    @Test
    fun `Given the db table is not empty, when getAllBeers() is called, then it should return all entities`() = runBlockingTest {

        // GIVEN
        val entityList = BeerEntityFactory.createBeerEntityList(count = 5)
        db.getBeerDao().addBeers(entityList)

        // WHEN
        val beerList = db.getBeerDao().getAllBeers()

        // THEN
        beerList.size `should equal` 5
        beerList `should equal` entityList
    }

    @Test
    fun `Given the db table is not empty, when getAllBeers() is called with a filter, then it should return the filtered entities`() = runBlockingTest {

        // GIVEN
        val entityList = BeerEntityFactory.createBeerEntityList(count = 5)
        val filter = "Beer 2"

        db.getBeerDao().addBeers(entityList)

        // WHEN
        val beerList = db.getBeerDao().getAllBeers(filterByName = filter)

        // THEN
        val expectedResult = listOf(entityList[2])
        beerList.size `should equal` 1
        beerList `should equal` expectedResult
    }

    @Test
    fun `Given the db table is empty, when getAllBeers() is called with a filter, then it should return an empty list`() = runBlockingTest {

        // WHEN
        val filter = "Beer 2"
        val beerList = db.getBeerDao().getAllBeers(filterByName = filter)

        // THEN
        beerList.size `should equal` 0
    }

    @Test
    fun `Given the db table is empty, when addBeers() is called, then it should insert all entities`() = runBlockingTest {

        // WHEN
        val entityList = BeerEntityFactory.createBeerEntityList(count = 5)
        db.getBeerDao().addBeers(entityList)

        // THEN
        val beerList = db.getBeerDao().getAllBeers()

        beerList.size `should equal` 5
        beerList `should equal` entityList
    }

    @Test
    fun `Given the db table is empty, when addBeer() is called, then it should insert the entity`() = runBlockingTest {

        // WHEN
        val entity = BeerEntityFactory.createBeerEntity()
        db.getBeerDao().addBeer(entity)

        // THEN
        val beerList = db.getBeerDao().getAllBeers()

        beerList.size `should equal` 1
        beerList[0] `should equal` entity
    }

    @Test
    fun `Given the db table is not empty, when addBeer() is called, then it should insert the entity if it does not exist`() = runBlockingTest {

        // GIVEN
        val entityList = BeerEntityFactory.createBeerEntityList(count = 5)
        db.getBeerDao().addBeers(entityList)

        // WHEN
        val entity = BeerEntityFactory.createBeerEntity(id = 6)
        db.getBeerDao().addBeer(entity)

        // THEN
        val beerList = db.getBeerDao().getAllBeers()

        beerList.size `should equal` 6
        beerList[5] `should equal` entity
    }

    @Test
    fun `Given the db table is not empty, when addBeer() is called, then it should replace the entity if exists`() = runBlockingTest {

        // GIVEN
        val entityList = BeerEntityFactory.createBeerEntityList(count = 5)
        db.getBeerDao().addBeers(entityList)

        // WHEN
        val newEntity = entityList[1].copy(tagline = "new tagline")
        db.getBeerDao().addBeer(newEntity)

        // THEN
        val beer = db.getBeerDao().getBeerById("1")

        beer.id `should equal` newEntity.id
        beer.tagline `should equal` newEntity.tagline
    }

    @Test
    fun `Given the db table is empty, when getBeerById() is called, then it should return null`() = runBlockingTest {

        // WHEN
        val beer = db.getBeerDao().getBeerById("1")

        // THEN
        beer `should equal` null
    }

    @Test
    fun `Given the db table is not empty, when getBeerById() is called, then it should return the entity`() = runBlockingTest {

        // GIVEN
        val entityList = BeerEntityFactory.createBeerEntityList(count = 5)
        db.getBeerDao().addBeers(entityList)

        // WHEN
        val beer = db.getBeerDao().getBeerById("1")

        // THEN
        beer `should equal` entityList[1]
    }

    @Test
    fun `Given the db table is not empty, when deleteAll() is called, then it should delete all entities from the table`() = runBlockingTest {

        // GIVEN
        val entityList = BeerEntityFactory.createBeerEntityList(count = 5)
        db.getBeerDao().addBeers(entityList)

        // WHEN
        db.getBeerDao().deleteAll()

        val beers = db.getBeerDao().getAllBeers()
        // THEN
        beers.size `should equal` 0
    }

    @After
    fun tearDown() {
        db.close()
    }
}