package edu.towson.cosc435.basaran.againsttheodds.api

import edu.towson.cosc435.basaran.againsttheodds.BuildConfig
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

const val apiKey = BuildConfig.API_KEY

// Request and response data models for OpenAI API

data class OpenAIRequest(
    val model: String = "gpt-4o-mini",
    val prompt: String,
    val temperature: Double = 0.7,
    val max_tokens: Int = 100
)

data class OpenAIResponse(
    val id: String,
    val api_object: String,
    val choices: List<Choice>
)

data class Choice(
    val message: Message,  // The message object with role and content
    val index: Int,
    val finish_reason: String
)


data class OpenAIChatRequest(
    val model: String,               // Model to use, like "gpt-3.5-turbo"
    val messages: List<Message>,     // List of messages (user input, system instructions, etc.)
    val temperature: Double = 0.7,   // Optional, controls randomness
    val max_tokens: Int = 100        // Optional, limits the number of tokens
)

data class Message(
    val role: String,                // "system", "user", or "assistant"
    val content: String              // The message content
)


interface OpenAIApiService {
    @Headers("Authorization: Bearer $apiKey")
    @POST("v1/chat/completions")
    suspend fun getResponse(@Body request: OpenAIChatRequest): OpenAIResponse
}