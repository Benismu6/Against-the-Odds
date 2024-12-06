package edu.towson.cosc435.basaran.againsttheodds.data

/**
 * Represents a request to the OpenAI API for text completion.
 */
data class OpenAIRequest(
    val model: String = "gpt-4o-mini",
    val prompt: String,
    val temperature: Double = 0.7,
    val max_tokens: Int = 100
)

/**
 * Represents the response from the OpenAI API for text completion.
 */
data class OpenAIResponse(
    val id: String,
    val api_object: String,
    val choices: List<Choice>
)

/**
 * Represents a single choice in the API response.
 */
data class Choice(
    val message: Message,
    val index: Int,
    val finish_reason: String
)

/**
 * Represents a request for chat completions to the OpenAI API.
 */
data class OpenAIChatRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Double = 0.7,
    val max_tokens: Int = 100
)

/**
 * Represents a single message in a conversation.
 */
data class Message(
    val role: String,
    val content: String
)
