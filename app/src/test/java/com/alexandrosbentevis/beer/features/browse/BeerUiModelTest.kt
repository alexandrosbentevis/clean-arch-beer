package com.alexandrosbentevis.beer.features.browse

import com.alexandrosbentevis.beer.factory.BeerFactory
import com.alexandrosbentevis.domain.models.Beer
import org.amshove.kluent.`should equal to`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class BeerUiModelTest {

    private lateinit var beer: Beer

    @Before
    fun setUp() {
        beer = BeerFactory.createBeer()
    }

    @Test
    fun `Mapper should map domain model to the ui model`() {
        // WHEN
        val data = beer.mapToBeerUiModel()

        // THEN
        data.id `should equal to` beer.id
        data.name `should equal to` beer.name
        data.imageUrl `should equal to` beer.imageUrl
        data.abv `should equal to` "${beer.abv}%"
        data.tagline `should equal to` beer.tagline
    }
}