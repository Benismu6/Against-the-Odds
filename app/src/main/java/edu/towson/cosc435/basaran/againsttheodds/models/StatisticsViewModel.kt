/**
 * StatisticsViewModel.kt
 *
 * This file defines the StatisticsViewModel, which handles the data for the statistics history
 * page. It fetches historical NFL data from the repository and exposes it to the
 * StatisticsFragment.
 *
 * Responsibilities:
 * - Fetches historical data about NFL games from the repository.
 * - Exposes this data to the StatisticsFragment via LiveData for display in the UI.
 *
 * Key Methods:
 */


package edu.towson.cosc435.basaran.againsttheodds.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.towson.cosc435.basaran.againsttheodds.api.NflApiService
import edu.towson.cosc435.basaran.againsttheodds.api.RetrofitInstance
import edu.towson.cosc435.basaran.againsttheodds.api.TeamStat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class StatisticsViewModel : ViewModel() {
    private val _availableSeasons = MutableLiveData<List<String>>()
    val availableSeasons: LiveData<List<String>> get() = _availableSeasons

    private val _teamStats = MutableLiveData<List<TeamStat>>()
    val teamStats: LiveData<List<TeamStat>> get() = _teamStats

    private val api: NflApiService = RetrofitInstance.api

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _serviceStatus = MutableLiveData<String>()
    val serviceStatus: LiveData<String> get() = _serviceStatus

    fun startService() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.startBackendService()
                if (response.isSuccessful) {
                    _serviceStatus.postValue("Service started successfully")
                } else {
                    _serviceStatus.postValue("Failed to start service: ${response.code()}")
                }
            } catch (e: Exception) {
                _serviceStatus.postValue("Error starting service: ${e.localizedMessage}")
            }
        }
    }

    fun fetchAvailableSeasons() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Simulate fetching seasons (modify this if your API has a dedicated endpoint for seasons)
                val allGamesResponse = api.getAllYearsGames().execute()
                val allYears = allGamesResponse.body()?.all_years_data?.keys?.toList() ?: emptyList()
                _availableSeasons.postValue(allYears)
            } catch (e: IOException) {
                e.printStackTrace() // Handle network error
            } catch (e: HttpException) {
                e.printStackTrace() // Handle HTTP error
            }
        }
    }

    fun fetchTeamStats(selectedSeason: String? = null) {
        viewModelScope.launch {
            _isLoading.postValue(true) // Show the progress indicator
            try {
                // Fetch data on the IO thread
                val games = withContext(Dispatchers.IO) {
                    if (selectedSeason == null || selectedSeason == "All Seasons") {
                        api.getAllYearsGames().execute().body()?.all_years_data?.values?.flatten() ?: emptyList()
                    } else {
                        api.getGamesByYear(selectedSeason.toInt()).execute().body()?.games ?: emptyList()
                    }
                }

                // Collect and compute stats
                val teams = games.flatMap { listOf(it.team1, it.team2) }.distinct()
                val stats = teams.map { team ->
                    val teamGames = games.filter { it.team1 == team || it.team2 == team }
                    val wins = teamGames.count { it.winner == team }
                    val losses = teamGames.size - wins
                    TeamStat(
                        team = team,
                        season = selectedSeason ?: "All Seasons",
                        wins = wins,
                        losses = losses
                    )
                }

                _teamStats.postValue(stats) // Update the stats
            } catch (e: IOException) {
                e.printStackTrace() // Handle network error
            } catch (e: HttpException) {
                e.printStackTrace() // Handle HTTP error
            } catch (e: Exception) {
                e.printStackTrace() // Handle unexpected errors
            } finally {
                delay(100L) // Optional: Slight delay for testing the indicator
                _isLoading.postValue(false) // Hide the progress indicator
            }
        }
    }

    fun clearData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.clearData().execute()
                if (response.isSuccessful) {
                    println("Clear data success: ${response.body()}")
                } else {
                    println("Error clearing data: ${response.code()}")
                }
            } catch (e: IOException) {
                println("Network error occurred:")
                e.printStackTrace() // Prints the full stack trace to the console
            } catch (e: HttpException) {
                println("HTTP error occurred:")
                e.printStackTrace() // Prints the full stack trace to the console
            } catch (e: Exception) {
                println("Unexpected error occurred:")
                e.printStackTrace() // Handles any unexpected errors
            }
        }
    }

    fun continueSimulation(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.simulateNextYear().execute()
                if (response.isSuccessful) {
                    val message = response.body()?.get("message") ?: "Simulation continued successfully."
                    onSuccess(message)
                } else {
                    val error = "Error continuing simulation: ${response.code()} - ${response.message()}"
                    onError(error)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                onError("Network error occurred while continuing simulation.")
            } catch (e: HttpException) {
                e.printStackTrace()
                onError("HTTP error occurred: ${e.message()}")
            } catch (e: Exception) {
                e.printStackTrace()
                onError("An unexpected error occurred: ${e.localizedMessage}")
            }
        }
    }
}
