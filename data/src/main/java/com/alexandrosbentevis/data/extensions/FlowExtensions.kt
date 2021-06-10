package com.alexandrosbentevis.data.extensions

import com.alexandrosbentevis.domain.framework.Result
import kotlinx.coroutines.flow.FlowCollector
import timber.log.Timber
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * Syncing mechanism to emit data from different sources. It emits the data from the db if available,
 * then it fetches the data from the api and compares them with the db. If the data is fresh, then
 * it emits the fresh data too.
 *
 * @param T the type of the data.
 *
 * @param forceRefresh flag for forcing a refresh of data.
 * @param loadFromDb lambda of how to load data from the cache.
 * @param saveToDb lambda of how to save data to the cache.
 * @param loadFromApi lambda of how load data from the api.
 *
 * @return the data wrapped in a [Result]
 */

suspend fun <T> FlowCollector<Result<T>>.emitData(
    forceRefresh: Boolean,
    loadFromDb: (suspend () -> T?),
    saveToDb: (suspend (T) -> Unit),
    loadFromApi: (suspend () -> T)
) {
    val fromDb: T? = when {
        forceRefresh -> null
        else -> loadFromDb()
    }
    fromDb?.let { emit(Result.Success(fromDb)) }
    try {
        val fromApi: T = loadFromApi()
        saveToDb(fromApi)
        val freshFromDb: T? = loadFromDb()
        if (freshFromDb != fromDb) {
            freshFromDb?.let { emit(Result.Success(freshFromDb)) }
        }
    } catch (exc: Exception) {
        if ((exc is UnknownHostException || exc is ConnectException) && fromDb != null) {
            Timber.e(exc)
        } else {
            emit(Result.Error(exc))
        }
    }
}
