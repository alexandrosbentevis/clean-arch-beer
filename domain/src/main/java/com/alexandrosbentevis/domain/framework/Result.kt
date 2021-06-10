package com.alexandrosbentevis.domain.framework

import java.lang.Exception

/**
 * The wrapper of the repository result.
 */
sealed class Result<out T> {
    /**
     * The success wrapper.
     *
     * @param data the data wrapped.
     */
    data class Success<out T>(val data: T): Result<T>()

    /**
     * The error wrapper.
     *
     * @param exc The exception of the error.
     */
    data class Error(val exc: Exception): Result<Nothing>()
}

