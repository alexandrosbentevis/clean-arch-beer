package com.alexandrosbentevis.beer.factory

import com.alexandrosbentevis.domain.models.Beer
import java.math.BigDecimal

class BeerFactory {

    companion object {

        fun createBeer(id: Int = 0): Beer = Beer(
            id = id.toString(),
            name = "Beer $id",
            imageUrl = "https://images.punkapi.com/v2/$id.png",
            abv = BigDecimal(id.toString()),
            tagline = "Awesome beer $id",
            description = "Description for beer $id",
            brewersTips = "Tips for beer $id",
            foodPairing = listOf(
                "Pairing 0 for beer $id",
                "Pairing 1 for beer $id"
            )
        )

        fun createBeerList(count: Int): List<Beer> {
            val list = mutableListOf<Beer>()
            repeat(count) {
                list.add(createBeer(id = it))
            }
            return list
        }
    }
}