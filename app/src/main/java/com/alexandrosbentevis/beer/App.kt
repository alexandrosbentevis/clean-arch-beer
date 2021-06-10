package com.alexandrosbentevis.beer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

/**
 * The Android application class.
 */
@HiltAndroidApp
class App @Inject constructor(): Application() {

    override fun onCreate() {
        super.onCreate()
        initializeLogger()
    }

    /**
     * Initializes the logger in debug builds.
     */
    private fun initializeLogger() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

}