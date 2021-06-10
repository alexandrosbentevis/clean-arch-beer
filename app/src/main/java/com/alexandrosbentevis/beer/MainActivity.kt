package com.alexandrosbentevis.beer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.alexandrosbentevis.beer.databinding.ActivityMainBinding
import com.alexandrosbentevis.beer.extensions.hide
import com.alexandrosbentevis.beer.extensions.show
import com.alexandrosbentevis.beer.framework.NetworkState
import com.alexandrosbentevis.beer.framework.NetworkStatusViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * The main activity of the app.
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private val networkViewModel: NetworkStatusViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupNetworkStatusObserver()
    }

    /**
     * Sets up the action bar.
     */
    private fun setupToolbar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.browse),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
        binding.toolbar.apply {
            setupWithNavController(navController, appBarConfiguration)
        }
    }

    /**
     * Sets up the network status observer.
     */
    private fun setupNetworkStatusObserver() {
        networkViewModel.state.observe(this, ::onNetworkStatusChanged )
    }

    /**
     * Callback for the network status.
     */
    private fun onNetworkStatusChanged(networkState: NetworkState?) {
        when (networkState) {
            NetworkState.Unavailable -> binding.networkStatusBanner.show()
            NetworkState.Available -> binding.networkStatusBanner.hide()
        }
    }

    /**
     * Expands the app bar.
     */
    fun expandToolbar() = binding.appBar.setExpanded(true, true)
}