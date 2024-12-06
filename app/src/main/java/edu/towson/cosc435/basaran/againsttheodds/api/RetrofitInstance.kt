package edu.towson.cosc435.basaran.againsttheodds.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Singleton instance for configuring Retrofit to interact with the NFL API.
 */
object RetrofitInstance {

    // Base URL for the NFL API
    private const val BASE_URL = "https://nfl-api.onrender.com"

    /**
     * Logging interceptor for HTTP requests and responses.
     * Logs the body of each HTTP request and response for debugging purposes.
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Logs complete request/response bodies
    }

    /**
     * Configured OkHttpClient with a logging interceptor.
     * Handles network calls and provides debug-level logs for API interactions.
     */
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Add the logging interceptor to the client
        .build()

    /**
     * Lazy-loaded Retrofit instance for accessing NflApiService.
     * Configures Retrofit with the base URL, HTTP client, and a Gson converter.
     */
    val api: NflApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Sets the base URL for all API endpoints
            .client(client) // Uses the custom OkHttpClient with logging
            .addConverterFactory(GsonConverterFactory.create()) // Parses JSON responses with Gson
            .build()
            .create(NflApiService::class.java) // Creates an implementation of the API service
    }
}
