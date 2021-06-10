package com.alexandrosbentevis.beer.features.browse

import com.alexandrosbentevis.domain.models.Beer

/**
 * Presentation layer class representing a beer model.
 *
 * @property id the id of the beer.
 * @property name the name of the beer.
 * @property imageUrl the image url of the beer.
 * @property abv the alcohol by volume of the beer.
 * @property tagline the tagline of the beer.
 */
data class BeerUiModel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val abv: String,
    val tagline: String
)

/**
 * Maps the domain model to a presentation model.
 */
fun Beer.mapToBeerUiModel() =
    BeerUiModel(
        id = id,
        name = name,
        imageUrl = imageUrl,
        abv = "$abv%",
        tagline = tagline
    )