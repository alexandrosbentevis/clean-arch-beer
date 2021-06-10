package com.alexandrosbentevis.beer.features.base

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.alexandrosbentevis.beer.MainActivity
import com.alexandrosbentevis.data.datasources.local.BeerDatabase
import dagger.hilt.android.testing.HiltAndroidRule
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.io.BufferedReader
import java.net.HttpURLConnection
import javax.inject.Inject

open class BaseUiTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var database: BeerDatabase

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        hiltRule.inject()

        mockWebServer = MockWebServer()
        mockWebServer.start(12345)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        database.close()
    }

    fun enqueueMockSuccessfulResponse(response: String) {
        mockWebServer.enqueue(
            MockResponse().setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(response))
    }

    fun enqueueMockErrorResponse() {
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR))
    }

    fun getJsonResponse(fileName: String) : String {
        val inputStream = InstrumentationRegistry.getInstrumentation().context.assets.open(fileName)
        return inputStream.bufferedReader().use(BufferedReader::readText)
    }
}