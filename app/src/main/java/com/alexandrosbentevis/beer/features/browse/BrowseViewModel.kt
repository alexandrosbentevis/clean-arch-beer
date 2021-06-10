package com.alexandrosbentevis.beer.features.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandrosbentevis.beer.framework.GetAllBeersFailure
import com.alexandrosbentevis.beer.framework.HybridLiveEvent
import com.alexandrosbentevis.beer.framework.UiState
import com.alexandrosbentevis.domain.framework.Result
import com.alexandrosbentevis.domain.usecases.GetAllBeersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The view model which handles the ui state.
 *
 * @property getAllBeersUseCase the use case associated with this view model.
 */
@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val getAllBeersUseCase: GetAllBeersUseCase
) : ViewModel() {

    private val _beerListState = HybridLiveEvent<UiState<List<BeerUiModel>>>().apply {
        value = UiState.Loading
    }
    val beerListState: LiveData<UiState<List<BeerUiModel>>> = _beerListState

    /**
     * Gets a list of beers.
     *
     * @param forceRefresh flag for forcing a refresh of data.
     * @param searchQuery Search query to filter.
     */
    fun getBeers(forceRefresh: Boolean = false, searchQuery: String = "") {
        viewModelScope.launch {
            try {
                getAllBeersUseCase(params = GetAllBeersUseCase.Params(forceRefresh = forceRefresh, filterByName = searchQuery.trim())).collect { result ->
                    when (result) {
                        is Result.Success -> _beerListState.value =
                            UiState.Empty.takeIf { result.data.isEmpty() }
                                ?: UiState.Success(result.data.map { it.mapToBeerUiModel() })
                        is Result.Error -> _beerListState.value =
                            GetAllBeersFailure(featureException = result.exc)
                    }
                }
            } catch (exc: Exception) {
                _beerListState.value = GetAllBeersFailure(featureException = exc)
            }
        }
    }
}