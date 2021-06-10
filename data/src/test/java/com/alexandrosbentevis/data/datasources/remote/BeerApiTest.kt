package com.alexandrosbentevis.data.datasources.remote

import com.alexandrosbentevis.data.datasources.remote.models.BeerDto
import com.google.gson.stream.MalformedJsonException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.amshove.kluent.`should equal`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class BeerApiTest {

    @get:Rule
    var exception: ExpectedException = ExpectedException.none()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var beerApi: BeerApi

    @Before
    fun setUp() {

        mockWebServer = MockWebServer()
        mockWebServer.start(port = 8080)

        beerApi = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .baseUrl(mockWebServer.url("/"))
            .build()
            .create(BeerApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Given the response is 200, when the getAllBeers() api is called, then it should be parsed as a Dto`() {
        runBlocking {
            // GIVEN
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(VALID_RESPONSE_BODY)
            mockWebServer.enqueue(response)

            // WHEN
            val data = beerApi.getAllBeers().first()

            // THEN
            data `should equal` BeerDto(
                id = 197,
                name = "Moshi Moshi 15",
                tagline = "American Pale Ale Birthday Beer.",
                abv = "5.2",
                imageUrl = "https://images.punkapi.com/v2/197.png",
                description = "The original BrewDog",
                brewersTips = "We recommend",
                foodPairing = listOf(
                    "Seared scallops",
                    "Pan seared venison with fruit salad side",
                    "Caramelised apple pie with vanilla ice cream"
                )
            )
        }
    }

    @Test
    fun `Given the response is 200, when the getBeerById() api is called, then it should be parsed as a Dto`() {
        runBlocking {
            // GIVEN
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(VALID_RESPONSE_BODY)
            mockWebServer.enqueue(response)

            // WHEN
            val data = beerApi.getBeerById("197").first()

            // THEN
            data `should equal` BeerDto(
                id = 197,
                name = "Moshi Moshi 15",
                tagline = "American Pale Ale Birthday Beer.",
                abv = "5.2",
                imageUrl = "https://images.punkapi.com/v2/197.png",
                description = "The original BrewDog",
                brewersTips = "We recommend",
                foodPairing = listOf(
                    "Seared scallops",
                    "Pan seared venison with fruit salad side",
                    "Caramelised apple pie with vanilla ice cream"
                )
            )
        }
    }

    @Test
    fun `Given the response is 200 with a malformed body, when the getAllBeers() api is called, an Exception should be thrown`() {
        runBlocking {
            // GIVEN
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MALFORMED_RESPONSE_BODY)
            mockWebServer.enqueue(response)

            exception.expect(MalformedJsonException::class.java)

            // WHEN
            beerApi.getAllBeers()
        }
    }

    @Test
    fun `Given the response is 200 with a malformed body, when the getBeerById() api is called, an Exception should be thrown`() {
        runBlocking {
            // GIVEN
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MALFORMED_RESPONSE_BODY)
            mockWebServer.enqueue(response)

            exception.expect(MalformedJsonException::class.java)

            // WHEN
            beerApi.getBeerById("197")
        }
    }

    @Test
    fun `Given the response is 500, when the getAllBeers() api is called, an Exception should be thrown `() {
        runBlocking {
            // GIVEN
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
            mockWebServer.enqueue(response)

            exception.expect(HttpException::class.java)

            // WHEN
            beerApi.getAllBeers()
        }
    }

    @Test
    fun `Given the response is 500, when the getBeerById() api is called, an Exception should be thrown `() {
        runBlocking {
            // GIVEN
            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
            mockWebServer.enqueue(response)

            exception.expect(HttpException::class.java)

            // WHEN
            beerApi.getBeerById("197")
        }
    }

    companion object {
        const val VALID_RESPONSE_BODY = """
            [
                {
                    "id": 197,
                    "name": "Moshi Moshi 15",
                    "tagline": "American Pale Ale Birthday Beer.",
                    "image_url": "https://images.punkapi.com/v2/197.png",
                    "abv": 5.2,
                    "description": "The original BrewDog",
                    "brewers_tips": "We recommend",
                    "food_pairing": [
                         "Seared scallops",
                         "Pan seared venison with fruit salad side",
                         "Caramelised apple pie with vanilla ice cream"
                    ]
                }
            ]
        """

        const val MALFORMED_RESPONSE_BODY = """
            [
                {
                    "id" 197,
                    "name" "Moshi Moshi 15",
                    "tagline" "American Pale Ale Birthday Beer.",
                    "image_url": "https://images.punkapi.com/v2/197.png",
                    "abv": 5.2
                }
            ]
        """
    }
}