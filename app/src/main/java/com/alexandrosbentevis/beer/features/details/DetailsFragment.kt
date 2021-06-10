package com.alexandrosbentevis.beer.features.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.alexandrosbentevis.beer.R
import com.alexandrosbentevis.beer.databinding.FragmentDetailsBinding
import com.alexandrosbentevis.beer.extensions.hide
import com.alexandrosbentevis.beer.extensions.load
import com.alexandrosbentevis.beer.extensions.show
import com.alexandrosbentevis.beer.features.base.BaseFragment
import com.alexandrosbentevis.beer.framework.UiState
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DetailsFragment : BaseFragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by viewModels()
    private val args:  DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        initializeSharedElementTransition()
    }

    /**
     * Initializes the shared element transition.
     */
    private fun initializeSharedElementTransition() {
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        ViewCompat.setTransitionName(binding.beerImageView, args.beerId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Setup of the mvvm observers.
     */
    override fun setupObservers() {
        viewModel.beerState.observe(this, ::onBeerDetailsStateChanged)
    }

    /**
     * Loads the data from the view model.
     *
     * @param forceRefresh flag for forcing a refresh of data.
     */
     private fun loadData(forceRefresh: Boolean = false) = viewModel.getBeer(forceRefresh = forceRefresh, id = args.beerId)

    /**
     * Initializes the views.
     */
    override fun initializeViews() {
        initializeSwipeRefreshLayout()
    }

    /**
     * Initializes the swipe to refresh view.
     */
    private fun initializeSwipeRefreshLayout() =
        binding.swipeRefreshLayout.setOnRefreshListener { loadData(forceRefresh = true) }

    /**
     * Callback which handles failures.
     *
     * @param failure the failure.
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
    private fun onBeerDetailsStateChanged(uiState: UiState<BeerDetailsUiModel>) {
        when (uiState) {
            is UiState.Loading -> renderLoadingState()
            is UiState.Success<BeerDetailsUiModel> -> renderSuccessState(data = uiState.data)
            is UiState.Failure -> onFailure(uiState)
        }
    }

    /**
     * Renders the loading state.
     */
    private fun renderLoadingState() {
        binding.progressBar.show()
    }

    /**
     * Renders the success state.
     *
     * @param data the data to be rendered.
     */
    private fun renderSuccessState(data: BeerDetailsUiModel) {
        binding.swipeRefreshLayout.isRefreshing = false
        binding.progressBar.hide()

        binding.beerNameTextView.text = data.name
        binding.beerAbvTextView.text = getString(R.string.abv, data.abv)
        binding.beerTaglineTextView.text = data.tagline
        binding.beerDescriptionTextView.text = data.description
        binding.beerBrewersTipsTextView.text = data.brewersTips
        binding.beerBrewersTipsTextView.text = data.brewersTips
        binding.beerFoodPairingTextView.text = data.foodPairing
        binding.beerImageView.load(
            url = data.imageUrl
        ) {
            startPostponedEnterTransition()
            binding.beerInfoLayout.animate().alpha(1.0f).duration = BEER_INFO_ALPHA_ANIMATION_DURATION
        }
    }

    companion object {
        const val BEER_INFO_ALPHA_ANIMATION_DURATION = 1000L
    }
}