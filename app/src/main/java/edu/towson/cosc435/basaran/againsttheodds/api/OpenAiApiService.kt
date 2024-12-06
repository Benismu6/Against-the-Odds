package edu.towson.cosc435.basaran.againsttheodds.api

import edu.towson.cosc435.basaran.againsttheodds.BuildConfig
import edu.towson.cosc435.basaran.againsttheodds.data.OpenAIChatRequest
import edu.towson.cosc435.basaran.againsttheodds.data.OpenAIResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// Constant for the OpenAI API key, fetched securely from BuildConfig
const val apiKey = BuildConfig.API_KEY

/**
 * Interface for the OpenAI API service.
 */
interface OpenAIApiService {

    /**
     * Sends a chat completion request to the OpenAI API.
     *
     * @param request The [OpenAIChatRequest] containing the model, messages, and other parameters.
     * @return The [OpenAIResponse] containing the AI's generated response.
     */
    @Headers("Authorization: Bearer $apiKey")
    @POST("v1/chat/completions")
    suspend fun getResponse(@Body request: OpenAIChatRequest): OpenAIResponse
}
