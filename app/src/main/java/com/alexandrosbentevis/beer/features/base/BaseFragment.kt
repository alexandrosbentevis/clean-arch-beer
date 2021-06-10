package com.alexandrosbentevis.beer.features.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.alexandrosbentevis.beer.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

abstract class BaseFragment(layoutId: Int): Fragment(layoutId) {

    /**
     * Initializes the views.
     */
    abstract fun initializeViews()

    /**
     * Setup of the mvvm observers.
     */
    abstract fun setupObservers()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expandAppBar()
        initializeViews()
        setupObservers()
    }

    /**
     * Expands the app bar.
     */
    @ExperimentalCoroutinesApi
    private fun expandAppBar() = (requireActivity() as MainActivity).expandToolbar()
}