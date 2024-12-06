package edu.towson.cosc435.basaran.againsttheodds.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.towson.cosc435.basaran.againsttheodds.api.Game
import edu.towson.cosc435.basaran.againsttheodds.api.Message
import edu.towson.cosc435.basaran.againsttheodds.api.OpenAIChatRequest
import edu.towson.cosc435.basaran.againsttheodds.api.OpenAIInstance
import edu.towson.cosc435.basaran.againsttheodds.api.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.tanh

class BettingOddsViewModel : ViewModel() {
    private val _confidence = MutableLiveData<Double?>()
    val confidence: LiveData<Double?> get() = _confidence

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _explanation = MutableLiveData<String>()
    val explanation: LiveData<String> get() = _explanation

    private val statisticsViewModel = StatisticsViewModel()
    private val _teamStats = statisticsViewModel.teamStats

    fun calculateConfidence(team1: String, team2: String, year: Int, overOrUnder: String, amount: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Fetch historical data
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getAllYearsGames().execute()
                }

                if (response.isSuccessful) {
                    val allYearsData = response.body()?.all_years_data ?: emptyMap()
                    val historicalGames = allYearsData.values.flatten()

                    // Analyze historical data
                    val team1Stats = calculateTeamStats(historicalGames, team1)
                    val team2Stats = calculateTeamStats(historicalGames, team2)

                    val expectedPoints = (team1Stats.avgPoints + team2Stats.avgPoints) / 2
                    val combinedSpread = (team1Stats.spread + team2Stats.spread) / 2

                    // Calculate confidence
                    val confidence = when (overOrUnder) {
                        "over" -> calculateOverConfidence(expectedPoints, amount, combinedSpread)
                        "under" -> calculateUnderConfidence(expectedPoints, amount, combinedSpread)
                        else -> 50.0
                    }

                    _confidence.value = confidence.coerceIn(0.0, 100.0)

                    // Now generate an explanation using OpenAI API
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

    private fun calculateOverConfidence(expectedPoints: Double, amount: Double, spread: Double): Double {
        val diff = expectedPoints - amount
        return scaleConfidence(diff, spread)
    }

    private fun calculateUnderConfidence(expectedPoints: Double, amount: Double, spread: Double): Double {
        val diff = amount - expectedPoints
        return scaleConfidence(diff, spread)
    }

    /**
     * Scales confidence using a sigmoid-like function to ensure smooth transitions.
     */
    private fun scaleConfidence(diff: Double, spread: Double): Double {
        val normalizedDiff = diff / (spread + 1e-6) // Avoid division by zero
        return 50 + (50 * tanh(normalizedDiff))
    }

    private data class TeamStats(
        val avgPoints: Double,
        val spread: Double
    )

    private fun calculateTeamStats(games: List<Game>, team: String): TeamStats {
        val teamGames = games.filter { it.team1 == team || it.team2 == team }

        val totalPoints = teamGames.sumOf { game ->
            if (game.team1 == team) game.score1 else game.score2
        }
        val gameCount = teamGames.size

        val avgPoints = if (gameCount > 0) totalPoints.toDouble() / gameCount else 0.0

        // Calculate spread (standard deviation of points)
        val variance = teamGames.sumOf { game ->
            val score = if (game.team1 == team) game.score1 else game.score2
            (score - avgPoints).pow(2)
        } / gameCount.toDouble()

        val spread = sqrt(variance)

        return TeamStats(avgPoints, spread)
    }

    // Example function to fetch the historical games for a team
    private fun fetchTeamGames(team: String): List<Game> {
        // Make an API call to fetch all games for all years
        val response = try {
            RetrofitInstance.api.getAllYearsGames().execute()
        } catch (e: Exception) {
            // Handle errors, e.g., network issues
            Log.e("FetchTeamGames", "Error fetching team games: ${e.message}")
            return emptyList()  // Return empty list in case of error
        }

        return if (response.isSuccessful) {
            val allYearsData = response.body()?.all_years_data ?: emptyMap()

            // Filter games for the specified team from all years data
            val teamGames = allYearsData.values.flatten().filter {
                it.team1 == team || it.team2 == team
            }

            teamGames
        } else {
            Log.e("FetchTeamGames", "Error fetching data: ${response.code()}")
            emptyList()  // Return empty list if the API call is unsuccessful
        }
    }


    // Function to calculate the average points scored by a team
    private fun calculateAveragePoints(games: List<Game>): Double {
        val totalPoints = games.sumOf { game -> if (game.team1 == games[0].team1) game.score1 else game.score2 }
        return if (games.isNotEmpty()) totalPoints.toDouble() / games.size else 0.0
    }

    // Function to calculate the average points conceded by a team
    private fun calculateAverageConceded(games: List<Game>): Double {
        val totalConceded = games.sumOf { game -> if (game.team1 == games[0].team1) game.score2 else game.score1 }
        return if (games.isNotEmpty()) totalConceded.toDouble() / games.size else 0.0
    }

    private fun generateExplanation(confidence: Double, team1: String, team2: String) {
        val prompt = """
            You have calculated a confidence level for an over/under bet as $confidence%. 
            Should the user make this bet? Are these good odds?
            Limit reponse to 30 words and round each number to the nearest whole number.
            Respond as though you are talking directly to the user.
            Be slightly humourous, while remaining professional.
        """

        // Construct the messages list
        val messages = listOf(
            Message(role = "system", content = "You are an expert assistant in sports statistics and betting."),
            Message(role = "user", content = prompt)  // User's input
        )

        // Create the OpenAI request with the messages
        val openAIChatRequest = OpenAIChatRequest(
            model = "gpt-3.5-turbo",  // Use the appropriate model
            messages = messages
        )

        // Now send the request using Retrofit
        viewModelScope.launch {
            try {
                // Make the API call
                val response = OpenAIInstance.api.getResponse(openAIChatRequest)

                if (response != null && response.choices.isNotEmpty()) {
                    // Extracting the explanation correctly
                    val explanation = response.choices.firstOrNull()?.message?.content
                        ?: "No explanation provided."
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
