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


package edu.towson.cosc435.basaran.againsttheodds.ui.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.towson.cosc435.basaran.againsttheodds.NflApiService
import edu.towson.cosc435.basaran.againsttheodds.RetrofitInstance
import edu.towson.cosc435.basaran.againsttheodds.TeamStat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class StatisticsViewModel : ViewModel() {
    private val _availableSeasons = MutableLiveData<List<String>>()
    val availableSeasons: LiveData<List<String>> get() = _availableSeasons

    private val _teamStats = MutableLiveData<List<TeamStat>>()
    val teamStats: LiveData<List<TeamStat>> get() = _teamStats

    private val api: NflApiService = RetrofitInstance.api

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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val games = if (selectedSeason == null || selectedSeason == "All Seasons") {
                    api.getAllYearsGames().execute().body()?.all_years_data?.values?.flatten() ?: emptyList()
                } else {
                    api.getGamesByYear(selectedSeason.toInt()).execute().body()?.games ?: emptyList()
                }

                // Collect all unique teams
                val teams = games.flatMap { listOf(it.team1, it.team2) }.distinct()

                // Calculate stats for all teams
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

                _teamStats.postValue(stats)
            } catch (e: IOException) {
                e.printStackTrace() // Handle network error
            } catch (e: HttpException) {
                e.printStackTrace() // Handle HTTP error
            }
        }
    }
}
