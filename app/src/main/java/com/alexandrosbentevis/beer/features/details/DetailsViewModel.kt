package com.alexandrosbentevis.beer.features.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandrosbentevis.beer.framework.GetBeerFailure
import com.alexandrosbentevis.beer.framework.HybridLiveEvent
import com.alexandrosbentevis.domain.usecases.GetBeerUseCase
import com.alexandrosbentevis.beer.framework.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import com.alexandrosbentevis.domain.framework.Result
import kotlinx.coroutines.launch

/**
 * The view model which handles the ui state.
 *
 * @property getBeerUseCase the use case associated with this view model.
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getBeerUseCase: GetBeerUseCase
) : ViewModel() {

    private val _beerState = HybridLiveEvent<UiState<BeerDetailsUiModel>>().apply {
        value = UiState.Loading
    }
    val beerState: LiveData<UiState<BeerDetailsUiModel>> = _beerState

    /**
     * Gets the details of a beer.
     *
     * @param forceRefresh flag for forcing a refresh of data
     * @param id the id of the beer.
     */
    fun getBeer(forceRefresh: Boolean = false, id: String) {
        viewModelScope.launch {
            try {
                getBeerUseCase(params = GetBeerUseCase.Params(forceRefresh = forceRefresh, id = id)).collect { result ->
                    when (result) {
                        is Result.Success -> _beerState.value = UiState.Success(result.data.mapToBeerDetailsUiModel())
                        is Result.Error -> _beerState.value = GetBeerFailure(featureException = result.exc)
                    }
                }
            } catch (exc: Exception) {
                _beerState.value = GetBeerFailure(featureException = exc)
            }
        }
    }
}