package edu.towson.cosc435.basaran.againsttheodds

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Data models to match the API responses
data class Team(
    val id: Int,
    val name: String,
    val bias: Double
)

data class Game(
    val team1: String,
    val team2: String,
    val score1: Int,
    val score2: Int,
    val winner: String
)

data class CurrentYearGamesResponse(
    val year: Int,
    val games: List<Game>
)

data class AllYearsGamesResponse(
    val all_years_data: Map<String, List<Game>>
)

data class YearGamesResponse(
    val year: Int,
    val games: List<Game>
)

data class TeamStat(
    val team: String,
    val season: String,
    val wins: Int,
    val losses: Int
)

// Define the API interface
interface NflApiService {
    @GET("/teams")
    fun getTeams(): Call<List<Team>>

    @GET("/games/current-year")
    fun getCurrentYearGames(): Call<CurrentYearGamesResponse>

    @GET("/games/all")
    fun getAllYearsGames(): Call<AllYearsGamesResponse>

    @GET("/games/{year}")
    fun getGamesByYear(@Path("year") year: Int): Call<YearGamesResponse>

    @POST("/clear-data")
    fun clearData(): Call<Map<String, String>>
}
