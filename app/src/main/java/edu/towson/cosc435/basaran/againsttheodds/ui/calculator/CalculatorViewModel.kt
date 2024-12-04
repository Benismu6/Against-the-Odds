package edu.towson.cosc435.basaran.againsttheodds.ui.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.towson.cosc435.basaran.againsttheodds.api.RetrofitInstance
import edu.towson.cosc435.basaran.againsttheodds.api.Game
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

}
