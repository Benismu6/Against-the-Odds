package edu.towson.cosc435.basaran.againsttheodds.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Configures and provides a Retrofit instance for the Team API.
 */
object TeamDescApiInstance {

    private const val BASE_URL = "https://www.thesportsdb.com/"

    // Logging interceptor for debugging API requests and responses
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient configured with the logging interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Retrofit instance configured with the base URL and OkHttpClient
    val api: TeamDescApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TeamDescApiService::class.java)
    }
}
