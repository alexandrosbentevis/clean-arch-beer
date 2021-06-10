package com.alexandrosbentevis.data.factory

import com.alexandrosbentevis.data.datasources.local.models.BeerEntity

class BeerEntityFactory {

    companion object {

        fun createBeerEntity(id: Int = 0): BeerEntity = BeerEntity(
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

        fun createBeerEntityList(count: Int): List<BeerEntity> {
            val list = mutableListOf<BeerEntity>()
            repeat(count) {
                list.add(createBeerEntity(id = it))
            }
            return list
        }
    }
}