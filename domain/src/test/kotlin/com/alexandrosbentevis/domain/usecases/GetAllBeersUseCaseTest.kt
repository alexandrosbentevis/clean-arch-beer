package com.alexandrosbentevis.domain.usecases

import com.alexandrosbentevis.domain.factory.BeerFactory
import com.alexandrosbentevis.domain.framework.CoroutinesTestRule
import com.alexandrosbentevis.domain.framework.Result
import com.alexandrosbentevis.domain.repositories.BeerRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should equal`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class GetAllBeersUseCaseTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private lateinit var useCase: GetAllBeersUseCase
    private lateinit var beerRepository: BeerRepository

    @Test
    fun `When the use case is invoked, then the repository should be called`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            val beerList = BeerFactory.createBeerList(count = 2)
            val params = GetAllBeersUseCase.Params(filterByName = "abc")

            beerRepository = mock {
                onBlocking { getBeers(any(), any()) } doReturn flow { emit(Result.Success(beerList)) }
            }

            useCase = GetAllBeersUseCase(beerRepository = beerRepository)

            // WHEN
            useCase(params = params)

            // THEN
            verify(beerRepository).getBeers(filterByName = params.filterByName)
        }
    }

    @Test
    fun `Given the repository returns a beer list wrapped in a success result, when the use case is invoked, then the success result should be returned`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            val beerList = BeerFactory.createBeerList(count = 2)
            val params = GetAllBeersUseCase.Params(filterByName = "abc")

            beerRepository = mock {
                onBlocking { getBeers(any(), any()) } doReturn flow { emit(Result.Success(beerList)) }
            }

            useCase = GetAllBeersUseCase(beerRepository = beerRepository)

            // WHEN
            val data = useCase(params = params).first()

            // THEN
            data `should be instance of` Result.Success::class.java
            data `should equal` Result.Success(beerList)
        }
    }

    @Test
    fun `Given the repository returns an error result, when the use case is invoked, then the error result should be returned`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            // GIVEN
            val exception = RuntimeException("Oops")
            val params = GetAllBeersUseCase.Params(filterByName = "abc")

            beerRepository = mock {
                onBlocking { getBeers(any(), any()) } doReturn flow { emit(Result.Error(exception)) }
            }

            useCase = GetAllBeersUseCase(beerRepository = beerRepository)

            // WHEN
            val data = useCase(params = params).first()

            // THEN
            data `should be instance of` Result.Error::class.java
            data `should equal` Result.Error(exception)
        }
    }
}

