package com.alexandrosbentevis.data.datasources.remote.models

import com.alexandrosbentevis.data.datasources.local.models.BeerEntity
import com.alexandrosbentevis.data.datasources.local.models.mapToDomain
import com.alexandrosbentevis.data.factory.BeerEntityFactory
import org.amshove.kluent.`should equal`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.math.BigDecimal

@RunWith(JUnit4::class)
class BeerEntityTest {

    private lateinit var entity: BeerEntity

    @Before
    fun setUp() {
        entity = BeerEntityFactory.createBeerEntity()
    }

    @Test
    fun `Given a beer entity, when mapped, then it should be mapped to a domain model`() {

        // WHEN
        val domainModel = entity.mapToDomain()

        // THEN
        domainModel.id `should equal` entity.id.toString()
        domainModel.name `should equal` entity.name
        domainModel.abv `should equal` BigDecimal(entity.abv)
        domainModel.imageUrl `should equal` entity.imageUrl
        domainModel.tagline `should equal` entity.tagline
        domainModel.description `should equal` entity.description
        domainModel.brewersTips `should equal` entity.brewersTips
        domainModel.foodPairing `should equal` entity.foodPairing
    }
}