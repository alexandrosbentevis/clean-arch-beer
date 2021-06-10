package com.alexandrosbentevis.beer.framework

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class NetworkStatusViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private lateinit var viewModel: NetworkStatusViewModel
    private lateinit var networkStatusProvider: NetworkStatusProvider

    @Test
    fun `Given the provider emits an unavailable state, when the view model state is observed, then the provider is invoked and the live data state changes to Unavailable`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            networkStatusProvider = mock {
                onBlocking { getNetworkStatus() } doReturn flow { emit(NetworkStatus.Unavailable) }
            }
            viewModel = NetworkStatusViewModel(networkStatusProvider)

            // WHEN
            val mockObserver : Observer<NetworkState> = mock()
            viewModel.state.observeForever(mockObserver)

            // THEN
            verify(networkStatusProvider).getNetworkStatus()
            verify(mockObserver).onChanged(NetworkState.Unavailable)
        }
    }

    @Test
    fun `Given the provider emits an available state, when the view model state is observed, then the provider is invoked and the live data state changes to Available`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            networkStatusProvider = mock {
                onBlocking { getNetworkStatus() } doReturn flow { emit(NetworkStatus.Available) }
            }
            viewModel = NetworkStatusViewModel(networkStatusProvider)

            // WHEN
            val mockObserver : Observer<NetworkState> = mock()
            viewModel.state.observeForever(mockObserver)

            // THEN
            verify(networkStatusProvider).getNetworkStatus()
            verify(mockObserver).onChanged(NetworkState.Available)
        }
    }
}