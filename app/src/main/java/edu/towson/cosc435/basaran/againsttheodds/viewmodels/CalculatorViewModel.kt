package edu.towson.cosc435.basaran.againsttheodds.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.towson.cosc435.basaran.againsttheodds.api.OpenAIInstance
import edu.towson.cosc435.basaran.againsttheodds.api.RetrofitInstance
import edu.towson.cosc435.basaran.againsttheodds.data.Game
import edu.towson.cosc435.basaran.againsttheodds.data.Message
import edu.towson.cosc435.basaran.againsttheodds.data.OpenAIChatRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.tanh

/**
 * ViewModel for handling betting odds calculations and generating betting advice.
 *
 * Responsibilities:
 * - Fetch and analyze historical game data.
 * - Calculate confidence levels for over/under bets.
 * - Generate betting advice using OpenAI.
 */
class BettingOddsViewModel : ViewModel() {
    // LiveData to expose confidence level to the UI
    private val _confidence = MutableLiveData<Double?>()
    val confidence: LiveData<Double?> get() = _confidence

    // LiveData for tracking loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // LiveData for error messages
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    // LiveData for explanation/advice
    private val _explanation = MutableLiveData<String>()
    val explanation: LiveData<String> get() = _explanation

    // Instance of StatisticsViewModel to reuse team statistics
    private val statisticsViewModel = StatisticsViewModel()
    private val _teamStats = statisticsViewModel.teamStats

    /**
     * Calculates the confidence level for an over/under bet.
     *
     * @param team1 The name of the first team.
     * @param team2 The name of the second team.
     * @param year The year of the match.
     * @param overOrUnder "over" or "under" for the bet type.
     * @param amount The over/under threshold.
     */
    fun calculateConfidence(team1: String, team2: String, year: Int, overOrUnder: String, amount: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Fetch historical data from the API
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getAllYearsGames().execute()
                }

                if (response.isSuccessful) {
                    val allYearsData = response.body()?.all_years_data ?: emptyMap()
                    val historicalGames = allYearsData.values.flatten()

                    // Analyze historical data for both teams
                    val team1Stats = calculateTeamStats(historicalGames, team1)
                    val team2Stats = calculateTeamStats(historicalGames, team2)

                    val expectedPoints = (team1Stats.avgPoints + team2Stats.avgPoints) / 2
                    val combinedSpread = (team1Stats.spread + team2Stats.spread) / 2

                    // Calculate confidence level based on bet type
                    val confidence = when (overOrUnder) {
                        "over" -> calculateOverConfidence(expectedPoints, amount, combinedSpread)
                        "under" -> calculateUnderConfidence(expectedPoints, amount, combinedSpread)
                        else -> 50.0 // Default confidence if no valid bet type is selected
                    }

                    _confidence.value = confidence.coerceIn(0.0, 100.0) // Ensure value is between 0 and 100

                    // Generate an explanation using OpenAI
                    generateExplanation(confidence, team1, team2)
                } else {
                    _errorMessage.value = "Error fetching data: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Calculates the confidence level for an "over" bet.
     */
    private fun calculateOverConfidence(expectedPoints: Double, amount: Double, spread: Double): Double {
        val diff = expectedPoints - amount
        return scaleConfidence(diff, spread)
    }

    /**
     * Calculates the confidence level for an "under" bet.
     */
    private fun calculateUnderConfidence(expectedPoints: Double, amount: Double, spread: Double): Double {
        val diff = amount - expectedPoints
        return scaleConfidence(diff, spread)
    }

    /**
     * Scales confidence using a sigmoid-like function for smooth transitions.
     */
    private fun scaleConfidence(diff: Double, spread: Double): Double {
        val normalizedDiff = diff / (spread + 1e-6) // Avoid division by zero
        return 50 + (50 * tanh(normalizedDiff))
    }

    /**
     * Data class for storing team statistics.
     */
    private data class TeamStats(
        val avgPoints: Double, // Average points scored by the team
        val spread: Double     // Standard deviation of points scored
    )

    /**
     * Calculates average points and spread (variance) for a specific team.
     *
     * @param games List of all historical games.
     * @param team The name of the team.
     * @return [TeamStats] containing average points and spread.
     */
    private fun calculateTeamStats(games: List<Game>, team: String): TeamStats {
        val teamGames = games.filter { it.team1 == team || it.team2 == team }

        val totalPoints = teamGames.sumOf { game ->
            if (game.team1 == team) game.score1 else game.score2
        }
        val gameCount = teamGames.size

        val avgPoints = if (gameCount > 0) totalPoints.toDouble() / gameCount else 0.0

        // Calculate spread (standard deviation)
        val variance = teamGames.sumOf { game ->
            val score = if (game.team1 == team) game.score1 else game.score2
            (score - avgPoints).pow(2)
        } / gameCount.toDouble()

        val spread = sqrt(variance)

        return TeamStats(avgPoints, spread)
    }

    /**
     * Generates an explanation/advice for the calculated confidence level using OpenAI.
     *
     * @param confidence The calculated confidence level.
     * @param team1 Name of the first team.
     * @param team2 Name of the second team.
     */
    private fun generateExplanation(confidence: Double, team1: String, team2: String) {
        val prompt = """
            You have calculated a confidence level for an over/under bet as $confidence%.
            Should the user make this bet? Are these good odds?
            Limit response to 30 words and round each number to the nearest whole number.
            Respond as though you are talking directly to the user.
            Be slightly humorous, while remaining professional.
        """.trimIndent()

        val messages = listOf(
            Message(role = "system", content = "You are an expert assistant in sports statistics and betting."),
            Message(role = "user", content = prompt)
        )

        val openAIChatRequest = OpenAIChatRequest(
            model = "gpt-3.5-turbo",
            messages = messages
        )

        // Send the request to OpenAI API
        viewModelScope.launch {
            try {
                val response = OpenAIInstance.api.getResponse(openAIChatRequest)
                if (response != null && response.choices.isNotEmpty()) {
                    val explanation = response.choices.firstOrNull()?.message?.content ?: "No explanation provided."
                    _explanation.value = explanation
                } else {
                    _errorMessage.value = "Error generating explanation: No response body."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error with OpenAI request: ${e.message}"
                Log.e("OpenAI API", "Error with OpenAI request: ${e.message}", e)
            }
        }
    }
}
