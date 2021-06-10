package com.alexandrosbentevis.beer.framework

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * The view model which handles the network connectivity state.
 */
@ExperimentalCoroutinesApi
@HiltViewModel
class NetworkStatusViewModel @Inject constructor(
    networkStatusProvider: NetworkStatusProvider
) : ViewModel() {

    val state = networkStatusProvider.getNetworkStatus().map(
                onUnavailable = { NetworkState.Unavailable },
                onAvailable = { NetworkState.Available },
            ).asLiveData()
}

/**
 * The network state.
 */
sealed class NetworkState {
    /**
     * Unavailable network state.
     */
    object Unavailable : NetworkState()

    /**
     * Available network state.
     */
    object Available : NetworkState()
}
