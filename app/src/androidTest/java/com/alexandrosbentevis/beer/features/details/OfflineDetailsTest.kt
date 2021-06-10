package com.alexandrosbentevis.beer.features.details

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.alexandrosbentevis.beer.R
import com.alexandrosbentevis.beer.di.NetworkStatusProviderAbstractModule
import com.alexandrosbentevis.beer.features.base.BaseUiTest
import com.alexandrosbentevis.beer.features.browse.BrowseRobot
import com.alexandrosbentevis.beer.framework.NetworkStatusProvider
import com.alexandrosbentevis.beer.framework.TestNetworkStatusProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Singleton

@UninstallModules(NetworkStatusProviderAbstractModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
class OfflineDetailsTest: BaseUiTest() {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class TestNetworkStatusProviderAbstractModule {

        @Singleton
        @Binds
        abstract fun bindNetworkStatusProvider(
            networkStatusProviderImpl: TestNetworkStatusProviderImpl
        ): NetworkStatusProvider
    }

    @Test
    fun givenOffline_whenTheDetailsScreenLoads_thenAnErrorBannerShouldBeDisplayed() {
        enqueueMockSuccessfulResponse(getJsonResponse(fileName = "beers-success.json"))
        enqueueMockSuccessfulResponse(getJsonResponse(fileName = "beer-success.json"))
        enqueueMockErrorResponse()

        BrowseRobot()
            .tapItemAtPosition(1)
            .assertNetworkUnavailableBannerShown(R.string.network_unavailable)
    }
}