package com.alexandrosbentevis.beer.features.details

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.alexandrosbentevis.beer.features.base.BaseUiTest
import com.alexandrosbentevis.beer.features.browse.BrowseRobot
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
class BrowseUiTest : BaseUiTest() {

    @Test
    fun givenTheUserTapsOnABeer_whenTheScreenLoads_thenTheBeerDetailsShouldBeShown() {
        enqueueMockSuccessfulResponse(getJsonResponse(fileName = "beers-success.json"))
        enqueueMockSuccessfulResponse(getJsonResponse(fileName = "beer-success.json"))

        BrowseRobot()
            .tapItemAtPosition(1)
            .assertBeerDetails(
                beerName = "Chaos Theory",
                beerTagline = "Predictably Random IPA.",
                beerAbv = "ABV 7.1%",
                beerDescription = "Chaos Theory is the most under- rated achievement of 20th Century science. This beer can only aspire to parallel to the mathematical use of the word chaos, which is at odds to the common parlance. The purest showcase of the magnificent hop that is Nelson Sauvin; grapefruit, pineapple and caramel sing above the chaos of life.",
                beerBrewersTips = "Get to know the guys in your local homebrew shop. They can give you a heads up when rare brewing materials – like Nelson Sauvin – are coming in.",
                foodPairing = listOf(
                    "Spicy mexican meatball stew",
                    "Habenero and mango pulled pork tacos",
                    "Spiced pumpkin pie with chocolate whipped cream"
                ).joinToString("\n")
            )
    }
    @Test

    fun givenThereIsAServerError_whenTheUserRefreshesOnDetails_thenAnErrorMessageShouldBeShown() {
        enqueueMockSuccessfulResponse(getJsonResponse(fileName = "beers-success.json"))
        enqueueMockSuccessfulResponse(getJsonResponse(fileName = "beer-success.json"))
        enqueueMockErrorResponse()

        BrowseRobot()
            .tapItemAtPosition(1)
            .swipeToRefresh()
            .assertErrorMessageShown("HTTP 500 Server Error")
    }
}