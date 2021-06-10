package com.alexandrosbentevis.beer.framework

/**
 * The user interface state.
 */
sealed class UiState<out T> {
    /**
     * Represents the state of loading.
     */
    object Loading : UiState<Nothing>()

    /**
     * Represents the empty state.
     */
    object Empty : UiState<Nothing>()

    /**
     * Represents the successful state.
     * @property data The data of the successful state.
     */
    data class Success<out T>(val data: T) : UiState<T>()

    /**
     * Represents the failure state.
     * @property exception The exception associated with the failure.
     */
    open class Failure(val exception: Exception) : UiState<Nothing>()
}

/**
 * Failure for getting the beers feature.
 *
 * @property featureException the exception associated with the failure.
 */
data class GetAllBeersFailure(val featureException: Exception) : UiState.Failure(featureException)

/**
 * Failure for getting a beer feature.
 *
 * @property featureException the exception associated with the failure.
 */
data class GetBeerFailure(val featureException: Exception) : UiState.Failure(featureException)