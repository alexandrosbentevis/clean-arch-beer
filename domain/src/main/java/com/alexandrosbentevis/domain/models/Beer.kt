package com.alexandrosbentevis.domain.models

import java.math.BigDecimal

/**
 * The beer domain class.
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
data class Beer(
    val id: String,
    val name: String,
    val imageUrl: String,
    val abv: BigDecimal,
    val tagline: String,
    val description: String,
    val brewersTips: String,
    val foodPairing: List<String>
)