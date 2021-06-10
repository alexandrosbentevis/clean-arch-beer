package com.alexandrosbentevis.data.datasources.remote.models

import com.alexandrosbentevis.domain.models.Beer
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * The Beer Data Access Object. It represents the json response.
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
data class BeerDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name")val name: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("abv") val abv: String,
    @SerializedName("tagline") val tagline: String,
    @SerializedName("description") val description: String,
    @SerializedName("brewers_tips") val brewersTips: String,
    @SerializedName("food_pairing") val foodPairing: List<String>
)

/**
 * Maps the Beer DTO into a domain model.
 */
fun BeerDto.mapToDomain(): Beer =
    Beer(
        id = id.toString(),
        name = name,
        imageUrl = imageUrl,
        abv = BigDecimal(abv),
        tagline = tagline,
        description = description,
        brewersTips = brewersTips,
        foodPairing = foodPairing
    )