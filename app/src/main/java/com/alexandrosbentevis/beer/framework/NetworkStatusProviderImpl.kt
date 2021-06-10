package com.alexandrosbentevis.beer.framework

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Interface for the network status provider.
 */
interface NetworkStatusProvider {

    /**
     * Returns the network status.
     *
     * @return The network status as a [Flow] of [NetworkStatus]
     */
    fun getNetworkStatus(): Flow<NetworkStatus>
}

/**
 * The implementation of the network status provider.
 */
@ExperimentalCoroutinesApi
class NetworkStatusProviderImpl @Inject constructor(
    @ApplicationContext context: Context,
    dispatcher: CoroutineDispatcher
) : NetworkStatusProvider {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkStatus = callbackFlow {
        val networkStatusCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onUnavailable() { trySend(NetworkStatus.Unavailable) }
            override fun onAvailable(network: Network) { trySend(NetworkStatus.Available) }
            override fun onLost(network: Network) { trySend(NetworkStatus.Unavailable) }
        }
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkStatusCallback)
        // initial value.
        when (connectivityManager.activeNetworkInfo?.isConnected ){
            true -> trySend(NetworkStatus.Available)
            else -> trySend(NetworkStatus.Unavailable)
        }
        awaitClose {
                connectivityManager.unregisterNetworkCallback(networkStatusCallback)
            }
        }.flowOn(dispatcher)

    override fun getNetworkStatus() = networkStatus

}

/**
 * Extension function for mapping the flow.
 */
fun <T> Flow<NetworkStatus>.map(
     onUnavailable: suspend () -> T,
     onAvailable: suspend () -> T,
): Flow<T> = map { status ->
    when (status) {
        NetworkStatus.Unavailable -> onUnavailable()
        NetworkStatus.Available -> onAvailable()
    }
}

/**
 * The networks status model. It can be available or unavailable.
 */
sealed class NetworkStatus {
    /**
     * The available network status.
     */
    object Available : NetworkStatus()

    /**
     * The unavailable network status.
     */
    object Unavailable : NetworkStatus()
}