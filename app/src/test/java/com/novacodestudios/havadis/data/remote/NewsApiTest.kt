package com.novacodestudios.havadis.data.remote

import com.novacodestudios.havadis.util.Constants.BASE_URL
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsApiTest {
    private lateinit var server: MockWebServer
    private lateinit var api: NewsApi

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        api = Retrofit.Builder()
            .baseUrl(server.url("/"))//Pass any base url like this
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(NewsApi::class.java)
    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `searchNews, return Success`() = runTest {
        // Mock sunucuya gerçekleştirilecek isteğin yanıtını ayarla
        val responseBody = "{\"articles\": [{\"author\": \"John Doe\", \"title\": \"Test Article\"}], \"status\": \"ok\", \"totalResults\": 1}"
        server.enqueue(MockResponse().setBody(responseBody))

        // API'ye istek gönder ve yanıtı al
        val response = api.searchNews(page = 1, searchQuery = "test_query", apikey = "test_api_key")

        // Beklenen sonucun döndürülüp dönmediğini kontrol et
        assertEquals(1, response.totalResults)
        assertEquals("ok", response.status)
        assertEquals(1, response.articles.size)
        assertEquals("John Doe", response.articles[0].author)
        assertEquals("Test Article", response.articles[0].title)
    }

    @Test
    fun getTopHedLinesNews() {
    }

    @Test
    fun getSources() {
    }
}