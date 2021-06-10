package com.alexandrosbentevis.beer.features.browse

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.alexandrosbentevis.beer.features.base.BaseUiTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
class BrowseUiTest : BaseUiTest() {

    @Test
    fun givenAnEmptyListOfBeers_whenTheScreenLoads_thenTheEmptyViewShouldBeShown() {
        enqueueMockSuccessfulResponse(getJsonResponse(fileName = "beers-success-empty.json"))

        BrowseRobot()
            .assertEmptyListViewDisplayed()
    }

    @Test
    fun givenAListOfBeers_whenTheScreenLoads_thenTheListShouldLoadOnTheScreen() {
        enqueueMockSuccessfulResponse(getJsonResponse(fileName = "beers-success.json"))

        BrowseRobot()
            .assertListSize(3)
            .scrollToPosition(position = 0)
            .assertBeerAtPosition(
                position = 0,
                beerName = "AB:03",
                beerTagline = "Barrel-Aged Imperial Ale.",
                beerAbv = "ABV 10.5%"
            )
            .scrollToPosition(position = 1)
            .assertBeerAtPosition(
                position = 1,
                beerName = "Chaos Theory",
                beerTagline = "Predictably Random IPA.",
                beerAbv = "ABV 7.1%"
            )
            .scrollToPosition(position = 2)
            .assertBeerAtPosition(
                position = 2,
                beerName = "Zephyr",
                beerTagline = "There’s A Storm Brewing.",
                beerAbv = "ABV 12.5%"
            )
    }

    @Test
    fun givenAListOfBeers_whenTheUserSearches_thenTheListShouldBeFiltered() {
        enqueueMockSuccessfulResponse(getJsonResponse(fileName = "beers-success.json"))

        BrowseRobot()
            .assertListSize(3)
            .typeToSearchEditText("Ze")
            .assertListSize(1)
            .scrollToPosition(position = 0)
            .assertBeerAtPosition(
                position = 0,
                beerName = "Zephyr",
                beerTagline = "There’s A Storm Brewing.",
                beerAbv = "ABV 12.5%"
            )
    }

    @Test
    fun givenThereIsAServerError_whenTheUserRefreshes_thenAnErrorMessageShouldBeShown() {
        enqueueMockSuccessfulResponse(getJsonResponse(fileName = "beers-success.json"))
        enqueueMockErrorResponse()

        BrowseRobot()
            .assertListSize(3)
            .swipeToRefresh()
            .assertErrorMessageShown("HTTP 500 Server Error")
    }
}