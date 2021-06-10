package com.alexandrosbentevis.data.datasources.local.models

import com.alexandrosbentevis.data.datasources.remote.models.BeerDto
import com.alexandrosbentevis.data.datasources.remote.models.mapToDomain
import com.alexandrosbentevis.data.factory.BeerDtoFactory
import org.amshove.kluent.`should equal`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.math.BigDecimal

@RunWith(JUnit4::class)
class BeerDtoTest {

    private lateinit var dto: BeerDto

    @Before
    fun setUp() {
        dto = BeerDtoFactory.createBeerDto()
    }

    @Test
    fun `Given a beer dto, when mapped, then it should be mapped to a domain model`() {

        // WHEN
        val domainModel = dto.mapToDomain()

        // THEN
        domainModel.id `should equal` dto.id.toString()
        domainModel.name `should equal` dto.name
        domainModel.abv `should equal` BigDecimal(dto.abv)
        domainModel.imageUrl `should equal` dto.imageUrl
        domainModel.tagline `should equal` dto.tagline
        domainModel.description `should equal` dto.description
        domainModel.brewersTips `should equal` dto.brewersTips
        domainModel.foodPairing `should equal` dto.foodPairing
    }
}