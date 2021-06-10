package com.alexandrosbentevis.data.datasources.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alexandrosbentevis.domain.models.Beer
import java.math.BigDecimal

/**
 * The beer database entity.
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
@Entity(tableName = "beers")
data class BeerEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "abv")
    val abv: String,

    @ColumnInfo(name = "tagline")
    val tagline: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "brewers_tips")
    val brewersTips: String,

    @ColumnInfo(name = "food_pairing")
    val foodPairing: List<String>
)

/**
 * Maps the Beer Entity into a domain model.
 */
fun BeerEntity.mapToDomain(): Beer =
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

/**
 * Maps the Beer domain model into a db model.
 */
fun Beer.mapToEntity(): BeerEntity =
    BeerEntity(
        id = id.toInt(),
        name = name,
        imageUrl = imageUrl,
        abv = abv.toString(),
        tagline = tagline,
        description = description,
        brewersTips = brewersTips,
        foodPairing = foodPairing
    )