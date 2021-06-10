package com.alexandrosbentevis.beer.features.browse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alexandrosbentevis.beer.factory.BeerFactory
import com.alexandrosbentevis.beer.framework.CoroutinesTestRule
import com.alexandrosbentevis.beer.framework.GetAllBeersFailure
import com.alexandrosbentevis.beer.framework.UiState
import com.alexandrosbentevis.domain.models.Beer
import com.alexandrosbentevis.domain.usecases.GetAllBeersUseCase
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.alexandrosbentevis.domain.framework.Result
import com.nhaarman.mockitokotlin2.any
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
class BrowseViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private lateinit var viewModel: BrowseViewModel
    private lateinit var getAllUseCase: GetAllBeersUseCase

    @Test
    fun `View model should return a loading state initially`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            getAllUseCase = mock()

            // WHEN
            viewModel = BrowseViewModel(getAllUseCase)

            // THEN
            viewModel.beerListState.value `should be instance of` UiState.Loading::class.java
            viewModel.beerListState.value `should equal` UiState.Loading
        }
    }

    @Test
    fun `Given the use case returns an empty list, when getBeers() is called, then the use case is invoked and the live data state changes to empty`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {

            // GIVEN
            getAllUseCase = mock {
                onBlocking { invoke(any()) } doReturn flow { emit(Result.Success(emptyList<Beer>())) }
            }
            viewModel = BrowseViewModel(getAllUseCase)

            val mockObserver : Observer<UiState<List<BeerUiModel>>> = mock()
            viewModel.beerListState.observeForever(mockObserver)

            // WHEN
            viewModel.getBeers()

            // THEN
            verify(getAllUseCase).invoke(GetAllBeersUseCase.Params(filterByName = ""))
            verify(mockObserver).onChanged(UiState.Empty)
        }
    }

    @Test
    fun `Given the use case returns a successful result, when getBeers() is called, then the use case is invoked and the live data state changes to success`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            val beerList = BeerFactory.createBeerList(5)
            getAllUseCase = mock {
                onBlocking { invoke(any()) } doReturn flow { emit(Result.Success(beerList)) }
            }
            viewModel = BrowseViewModel(getAllUseCase)

            val mockObserver : Observer<UiState<List<BeerUiModel>>> = mock()
            viewModel.beerListState.observeForever(mockObserver)

            // WHEN
            viewModel.getBeers(searchQuery = "searchQuery")

            // THEN
            verify(mockObserver).onChanged(UiState.Success(beerList.map { it.mapToBeerUiModel() }))
            verify(getAllUseCase).invoke(GetAllBeersUseCase.Params(filterByName = "searchQuery"))
        }
    }

    @Test
    fun `Given the use case returns an error result, when getBeers() is called, then the use case is invoked and the live data state changes to failure`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            val exception = RuntimeException("Exception")
            getAllUseCase = mock {
                onBlocking { invoke(any()) } doReturn flow { emit(Result.Error(exception)) }
            }
            viewModel = BrowseViewModel(getAllUseCase)

            val mockObserver : Observer<UiState<List<BeerUiModel>>> = mock()
            viewModel.beerListState.observeForever(mockObserver)

            // WHEN
            viewModel.getBeers(searchQuery = "searchQuery")

            // THEN
            verify(mockObserver).onChanged(GetAllBeersFailure(exception))
            verify(getAllUseCase).invoke(GetAllBeersUseCase.Params(filterByName = "searchQuery"))
        }
    }
}

