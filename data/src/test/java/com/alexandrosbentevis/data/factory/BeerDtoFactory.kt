package com.alexandrosbentevis.data.factory

import com.alexandrosbentevis.data.datasources.remote.models.BeerDto

class BeerDtoFactory {

    companion object {

        fun createBeerDto(id: Int = 0): BeerDto = BeerDto(
            id = id,
            name = "Beer $id",
            imageUrl = "https://images.punkapi.com/v2/$id.png",
            abv = id.toString(),
            tagline = "Awesome beer $id",
            description = "Description for beer $id",
            brewersTips = "Tips for beer $id",
            foodPairing = listOf(
                "Pairing 0 for beer $id",
                "Pairing 1 for beer $id"
            )
        )

        fun createBeerDtoList(count: Int): List<BeerDto> {
            val list = mutableListOf<BeerDto>()
            repeat(count) {
                list.add(createBeerDto(id = it))
            }
            return list
        }
    }
}