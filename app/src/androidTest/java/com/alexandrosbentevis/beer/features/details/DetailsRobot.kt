package com.alexandrosbentevis.beer.features.details

import androidx.annotation.StringRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.alexandrosbentevis.beer.R
import com.alexandrosbentevis.beer.features.browse.BrowseRobot
import com.alexandrosbentevis.beer.framework.ToastMatcher
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaSwipeRefreshInteractions.refresh
import java.lang.Thread.sleep

class DetailsRobot {

    fun swipeToRefresh(): DetailsRobot {
        sleep(500)
        refresh(R.id.swipeRefreshLayout)
        return DetailsRobot()
    }

    fun assertBeerDetails(
        beerName: String,
        beerTagline: String,
        beerAbv: String,
        beerDescription: String,
        beerBrewersTips: String,
        foodPairing: String
    ): DetailsRobot {
        assertDisplayed(R.id.beerNameTextView, beerName)
        assertDisplayed(R.id.beerAbvTextView, beerAbv)
        assertDisplayed(R.id.beerTaglineTextView, beerTagline)
        assertDisplayed(R.id.beerDescriptionTextView, beerDescription)
        assertDisplayed(R.id.beerBrewersTipsTextView, beerBrewersTips)
        assertDisplayed(R.id.beerFoodPairingTextView, foodPairing)
        return DetailsRobot()
    }

    fun assertNetworkUnavailableBannerShown(@StringRes errorMessageRes: Int): DetailsRobot {
        assertDisplayed(R.id.networkStatusBanner, errorMessageRes)
        return DetailsRobot()
    }

    fun assertErrorMessageShown(errorMessage: String): BrowseRobot {
        Espresso.onView(ViewMatchers.withText(errorMessage))
            .inRoot(ToastMatcher()).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        return BrowseRobot()
    }
}