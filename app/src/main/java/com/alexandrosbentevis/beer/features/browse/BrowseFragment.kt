package com.alexandrosbentevis.beer.features.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexandrosbentevis.beer.R
import com.alexandrosbentevis.beer.databinding.FragmentBrowseBinding
import com.alexandrosbentevis.beer.extensions.hide
import com.alexandrosbentevis.beer.extensions.show
import com.alexandrosbentevis.beer.extensions.textChanges
import com.alexandrosbentevis.beer.features.base.BaseFragment
import com.alexandrosbentevis.beer.framework.MarginItemDecoration
import com.alexandrosbentevis.beer.framework.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

/**
 * Screen for browsing beers.
 */
@AndroidEntryPoint
class BrowseFragment : BaseFragment(R.layout.fragment_browse) {

    private var _binding: FragmentBrowseBinding? = null
    private val binding get() = _binding!!

    private lateinit var beerAdapter: BeerAdapter

    private val viewModel: BrowseViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        postponeEnterTransition()
        _binding = FragmentBrowseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Loads the data from the view model.
     *
     * @param forceRefresh flag for forcing a refresh of data
     * @param searchQuery The query string for filtering the results.
     */
    private fun loadData(forceRefresh: Boolean = false, searchQuery: String) = viewModel.getBeers(forceRefresh = forceRefresh, searchQuery = searchQuery)

    /**
     * Initializes the views.
     */
    override fun initializeViews() {
        initializeSwipeRefreshLayout()
        initializeRecyclerView()
        initializeSearchEditText()
    }

    @FlowPreview
    private fun initializeSearchEditText() {
        binding.searchEditText.textChanges()
            .debounce(EDIT_TEXT_DEBOUNCE_TIMEOUT_MILLIS)
            .onEach { loadData(searchQuery = it.toString()) }
            .launchIn(lifecycleScope)
    }

    /**
     * Setup of the mvvm observers.
     */
    override fun setupObservers() {
        viewModel.beerListState.observe(this, ::onBeerListStateChanged)
    }

    /**
     * Initializes the recycler view.
     */
    private fun initializeRecyclerView() {
        beerAdapter = BeerAdapter(
            action = ::navigateToBeerDetails
        )
        val manager = LinearLayoutManager(requireContext())
        binding.recyclerView.apply {
            layoutManager = manager
            adapter = beerAdapter
            addItemDecoration(
                MarginItemDecoration(resources.getDimension(R.dimen.list_item_decoration_padding).toInt())
            )
        }
        beerAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    /**
     * Initializes the swipe to refresh view.
     */
    private fun initializeSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadData(forceRefresh = true, searchQuery = binding.searchEditText.text.toString())
        }
    }

    /**
     * Callback which handles failures.
     *
     * @param failure the failure to be handled.
     */
     private fun onFailure(failure: UiState.Failure) {
        Timber.e(failure.exception)
        binding.swipeRefreshLayout.isRefreshing = false
        binding.progressBar.hide()
        Toast.makeText(requireContext(), failure.exception.message.toString(), Toast.LENGTH_SHORT).show()
    }

    /**
     * Callback which handles the ui state.
     *
     * @param uiState the ui state.
     */
    private fun onBeerListStateChanged(uiState: UiState<List<BeerUiModel>>) {
        when (uiState) {
            is UiState.Loading -> renderLoadingState()
            is UiState.Empty -> renderEmptyState()
            is UiState.Success<List<BeerUiModel>> -> renderSuccessState(data = uiState.data)
            is UiState.Failure -> onFailure(uiState)
        }
    }

    /**
     * Renders the loading state.
     */
    private fun renderLoadingState() {
        binding.progressBar.show()
        binding.emptyImageView.hide()
    }

    /**
     * Renders the empty state.
     */
    private fun renderEmptyState() {
        binding.swipeRefreshLayout.isRefreshing = false
        binding.progressBar.hide()
        binding.emptyImageView.show()
        beerAdapter.items = emptyList()
        startPostponedEnterTransition()
    }

    /**
     * Renders the success state.
     *
     * @param data the data.
     */
    private fun renderSuccessState(data: List<BeerUiModel>) {
        binding.swipeRefreshLayout.isRefreshing = false
        binding.progressBar.hide()
        binding.emptyImageView.hide()
        binding.recyclerView.show()
        beerAdapter.items = data
        startPostponedEnterTransition()
    }

    /**
     * Navigates to the details screen.
     *
     * @param beerUiModel the presentation model of the selected item of the list.
     */
    private fun navigateToBeerDetails(beerUiModel: BeerUiModel, beerImageView: ImageView) {
        val directions = BrowseFragmentDirections.toDetails(beerUiModel.id)
        val extras = FragmentNavigatorExtras(
            beerImageView to beerImageView.transitionName
        )
        findNavController().navigate(directions, extras)
    }

    companion object {
        const val EDIT_TEXT_DEBOUNCE_TIMEOUT_MILLIS = 300L
    }
}