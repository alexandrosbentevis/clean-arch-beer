package com.alexandrosbentevis.beer.framework

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A lifecycle-aware observable that sends only new updates after subscription.
 * This live data only calls the observable if there is an explicit call to setValue() or call().
 *
 * Only one observer is going to be notified of changes.
 */
class HybridLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)
    private val sticky = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, { t ->
            if (sticky.get() || pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }

    override fun postValue(value: T?) {
        postValue(value, false)
    }

    private fun postValue(value: T?, sticky: Boolean = false) {
        this.sticky.set(sticky)
        super.postValue(value)
    }
}