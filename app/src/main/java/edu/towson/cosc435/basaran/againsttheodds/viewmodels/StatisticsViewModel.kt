package edu.towson.cosc435.basaran.againsttheodds.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.towson.cosc435.basaran.againsttheodds.api.NflApiService
import edu.towson.cosc435.basaran.againsttheodds.api.RetrofitInstance
import edu.towson.cosc435.basaran.againsttheodds.data.TeamStat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

/**
 * ViewModel for managing NFL statistics, including fetching team stats, seasons, and handling backend operations.
 */
class StatisticsViewModel : ViewModel() {

    // MutableLiveData for available seasons
    private val _availableSeasons = MutableLiveData<List<String>>()
    /** Public LiveData for observing available seasons. */
    val availableSeasons: LiveData<List<String>> get() = _availableSeasons

    // MutableLiveData for team stats
    private val _teamStats = MutableLiveData<List<TeamStat>>()
    /** Public LiveData for observing team statistics. */
    val teamStats: LiveData<List<TeamStat>> get() = _teamStats

    // Instance of the API service
    private val api: NflApiService = RetrofitInstance.api

    // MutableLiveData for loading state
    private val _isLoading = MutableLiveData(false)
    /** Public LiveData for observing the loading state. */
    val isLoading: LiveData<Boolean> get() = _isLoading

    // MutableLiveData for backend service status
    private val _serviceStatus = MutableLiveData<String>()
    /** Public LiveData for observing backend service status messages. */
    val serviceStatus: LiveData<String> get() = _serviceStatus

    /**
     * Starts the backend service.
     * Updates [serviceStatus] with the result of the operation.
     */
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

    /**
     * Fetches all available seasons from the API.
     * Updates [availableSeasons] with the retrieved list.
     */
    fun fetchAvailableSeasons() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.getAllYearsGames().execute()
                val allYears = response.body()?.all_years_data?.keys?.toList() ?: emptyList()
                _availableSeasons.postValue(allYears)
            } catch (e: IOException) {
                e.printStackTrace() // Handle network error
            } catch (e: HttpException) {
                e.printStackTrace() // Handle HTTP error
            }
        }
    }

    /**
     * Fetches team statistics for a given season.
     * If no season is specified, fetches statistics for all seasons.
     *
     * @param selectedSeason The season to fetch stats for, or null for all seasons.
     */
    fun fetchTeamStats(selectedSeason: String? = null) {
        viewModelScope.launch {
            _isLoading.postValue(true) // Show loading spinner
            try {
                // Fetch data from the API
                val games = withContext(Dispatchers.IO) {
                    if (selectedSeason == null || selectedSeason == "All Seasons") {
                        api.getAllYearsGames().execute().body()?.all_years_data?.values?.flatten() ?: emptyList()
                    } else {
                        api.getGamesByYear(selectedSeason.toInt()).execute().body()?.games ?: emptyList()
                    }
                }

                // Process team statistics
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

                _teamStats.postValue(stats) // Update team stats
            } catch (e: IOException) {
                e.printStackTrace() // Handle network error
            } catch (e: HttpException) {
                e.printStackTrace() // Handle HTTP error
            } catch (e: Exception) {
                e.printStackTrace() // Handle unexpected errors
            } finally {
                delay(100L) // Optional delay for smoother UI transition
                _isLoading.postValue(false) // Hide loading spinner
            }
        }
    }

    /**
     * Clears backend data via the API.
     * Logs success or error messages.
     */
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
                e.printStackTrace() // Prints full stack trace
            } catch (e: HttpException) {
                println("HTTP error occurred:")
                e.printStackTrace()
            } catch (e: Exception) {
                println("Unexpected error occurred:")
                e.printStackTrace()
            }
        }
    }

    /**
     * Continues the simulation by requesting the backend to simulate the next year.
     * Calls [onSuccess] if the simulation is successful, or [onError] if an error occurs.
     *
     * @param onSuccess Callback for successful simulation continuation.
     * @param onError Callback for handling errors during simulation.
     */
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
