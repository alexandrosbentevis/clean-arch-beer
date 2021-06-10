package com.alexandrosbentevis.beer.framework

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Mock Network Status Provider.
 */
class TestNetworkStatusProviderImpl @Inject constructor(
    @ApplicationContext context: Context,
    dispatcher: CoroutineDispatcher
) : NetworkStatusProvider {

    /**
     * The status is mocked as Unavailable.
     */
    private val networkStatus = flow {
        emit(NetworkStatus.Unavailable)
    }.flowOn(dispatcher)

    override fun getNetworkStatus() = networkStatus
}