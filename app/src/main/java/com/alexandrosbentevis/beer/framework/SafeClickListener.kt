package com.alexandrosbentevis.beer.framework

import android.os.SystemClock
import android.view.View

/**
 * Click listener preserve the last click time and prevent clicking for a specific time period.
 *
 * @property defaultInterval the specific time period.
 * @property onSafeCLick lambda of the click action.
 */
class SafeClickListener(
    private var defaultInterval: Int = 1000,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}