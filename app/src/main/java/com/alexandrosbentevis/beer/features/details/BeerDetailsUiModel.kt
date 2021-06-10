package com.alexandrosbentevis.beer.features.details

import com.alexandrosbentevis.domain.models.Beer

/**
 * Presentation layer class representing a beer details model.
 *
 * @property id the id of the beer.
 * @property name the name of the beer.
 * @property imageUrl the image url of the beer.
 * @property abv the alcohol by volume of the beer.
 * @property tagline the tagline of the beer.
 * @property description the description of the beer.
 * @property brewersTips tips from the brewers.
 * @property foodPairing list of food pairing with the beer.
 */
data class BeerDetailsUiModel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val abv: String,
    val tagline: String,
    val description: String,
    val brewersTips: String,
    val foodPairing: CharSequence
)

/**
 * Maps the domain model to a presentation model.
 */
fun Beer.mapToBeerDetailsUiModel() =
    BeerDetailsUiModel(
        id = id,
        name = name,
        imageUrl = imageUrl,
        abv = "$abv%",
        tagline = tagline,
        description = description,
        brewersTips = brewersTips,
        foodPairing = foodPairing.joinToString("\n")
    )