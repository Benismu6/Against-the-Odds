package edu.towson.cosc435.basaran.againsttheodds.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton instance for configuring Retrofit to interact with the OpenAI API.
 */
object OpenAIInstance {

    // Base URL for the OpenAI API
    private const val BASE_URL = "https://api.openai.com/"

    /**
     * Logging interceptor for HTTP requests and responses.
     * Logs the body of the HTTP request/response for debugging purposes.
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Log the entire body of HTTP requests/responses
    }

    /**
     * Configured OkHttpClient with logging enabled.
     * Handles network calls with added logging for debugging API interactions.
     */
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Attach logging interceptor
        .build()

    /**
     * Lazy-loaded Retrofit instance for accessing OpenAIApiService.
     * Configures Retrofit with the base URL, HTTP client, and a Gson converter.
     */
    val api: OpenAIApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Set the base URL for API requests
            .client(client) // Use the configured OkHttpClient
            .addConverterFactory(GsonConverterFactory.create()) // Parse JSON responses with Gson
            .build()
            .create(OpenAIApiService::class.java) // Create an implementation of OpenAIApiService
    }
}
