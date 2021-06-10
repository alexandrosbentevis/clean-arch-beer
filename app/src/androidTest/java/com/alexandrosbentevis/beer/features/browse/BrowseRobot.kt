package com.alexandrosbentevis.beer.features.browse

import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.alexandrosbentevis.beer.R
import com.alexandrosbentevis.beer.features.details.DetailsRobot
import com.alexandrosbentevis.beer.framework.ToastMatcher
import com.schibsted.spain.barista.assertion.BaristaListAssertions
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import com.schibsted.spain.barista.interaction.BaristaListInteractions
import com.schibsted.spain.barista.interaction.BaristaSwipeRefreshInteractions.refresh
import java.lang.Thread.sleep

class BrowseRobot {

    fun scrollToPosition(position: Int): BrowseRobot {
        BaristaListInteractions.scrollListToPosition(R.id.recyclerView, position)
        sleep(500)
        return BrowseRobot()
    }

    fun typeToSearchEditText(searchQuery: String): BrowseRobot {
        writeTo(R.id.searchEditText, searchQuery)
        return BrowseRobot()
    }

    fun swipeToRefresh(): BrowseRobot {
        refresh(R.id.swipeRefreshLayout)
        return BrowseRobot()
    }

    fun tapItemAtPosition(position: Int): DetailsRobot {
        sleep(500)
        BaristaListInteractions.clickListItem(R.id.recyclerView, position)
        sleep(500)
        return DetailsRobot()
    }

    fun assertListSize(numOfItems: Int): BrowseRobot {
        sleep(500)
        BaristaListAssertions.assertListItemCount(R.id.recyclerView, numOfItems)
        return BrowseRobot()
    }

    fun assertBeerAtPosition(
        position: Int,
        beerName: String,
        beerTagline: String,
        beerAbv: String
    ): BrowseRobot {
        assertDisplayedAtPosition(R.id.recyclerView, position, R.id.beerNameTextView, beerName)
        assertDisplayedAtPosition(
            R.id.recyclerView,
            position,
            R.id.beerTaglineTextView,
            beerTagline
        )
        assertDisplayedAtPosition(R.id.recyclerView, position, R.id.beerAbvTextView, beerAbv)
        return BrowseRobot()
    }

    fun assertEmptyListViewDisplayed(): BrowseRobot {
        sleep(500)
        assertDisplayed(R.id.emptyImageView)
        return BrowseRobot()
    }

    fun assertNetworkUnavailableBannerShown(@StringRes errorMessageRes: Int): BrowseRobot {
        assertDisplayed(R.id.networkStatusBanner, errorMessageRes)
        return BrowseRobot()
    }

    fun assertErrorMessageShown(errorMessage: String): BrowseRobot {
        onView(withText(errorMessage))
            .inRoot(ToastMatcher()).check(matches(isDisplayed()))
        return BrowseRobot()
    }
}
