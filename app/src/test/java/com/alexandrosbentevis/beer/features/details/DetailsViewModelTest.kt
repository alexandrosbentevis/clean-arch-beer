package com.alexandrosbentevis.beer.features.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alexandrosbentevis.beer.factory.BeerFactory
import com.alexandrosbentevis.beer.framework.CoroutinesTestRule
import com.alexandrosbentevis.beer.framework.GetBeerFailure
import com.alexandrosbentevis.beer.framework.UiState
import com.alexandrosbentevis.domain.framework.Result
import com.alexandrosbentevis.domain.usecases.GetBeerUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should equal`
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DetailsViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private lateinit var viewModel: DetailsViewModel
    private lateinit var getBeerUseCase: GetBeerUseCase

    @Test
    fun `View model should return a loading state initially`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            getBeerUseCase = mock()

            // WHEN
            viewModel = DetailsViewModel(getBeerUseCase)

            // THEN
            viewModel.beerState.value `should be instance of` UiState.Loading::class.java
            viewModel.beerState.value `should equal` UiState.Loading
        }
    }


    @Test
    fun `Given the use case returns a successful result, when getBeer() is called, then the use case is invoked and the live data state changes to success`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            val beer = BeerFactory.createBeer()
            getBeerUseCase = mock {
                onBlocking { invoke(any()) } doReturn flow { emit(Result.Success(beer)) }
            }
            viewModel = DetailsViewModel(getBeerUseCase)

            val mockObserver : Observer<UiState<BeerDetailsUiModel>> = mock()
            viewModel.beerState.observeForever(mockObserver)

            // WHEN
            viewModel.getBeer(id = beer.id)

            // THEN
            verify(getBeerUseCase).invoke(GetBeerUseCase.Params(id = beer.id))
            verify(mockObserver).onChanged(UiState.Success(beer.mapToBeerDetailsUiModel() ))
        }
    }

    @Test
    fun `Given the use case returns an error result, when getBeer() is called, then the use case is invoked and the live data state changes to failure`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            val exception = RuntimeException("Exception")
            val id = "0"

            getBeerUseCase = mock {
                onBlocking { invoke(any()) } doReturn flow { emit(Result.Error(exception)) }
            }
            viewModel = DetailsViewModel(getBeerUseCase)

            val mockObserver : Observer<UiState<BeerDetailsUiModel>> = mock()
            viewModel.beerState.observeForever(mockObserver)

            // WHEN
            viewModel.getBeer(id = id)

            // THEN
            verify(getBeerUseCase).invoke(GetBeerUseCase.Params(id = id))
            verify(mockObserver).onChanged(GetBeerFailure(exception))
        }
    }
}