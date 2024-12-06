package edu.towson.cosc435.basaran.againsttheodds.api

import edu.towson.cosc435.basaran.againsttheodds.data.AllYearsGames
import edu.towson.cosc435.basaran.againsttheodds.data.CurrentYearGames
import edu.towson.cosc435.basaran.againsttheodds.data.Team
import edu.towson.cosc435.basaran.againsttheodds.data.YearGamesResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Service interface for interacting with the NFL API.
 */
interface NflApiService {

    /**
     * Starts the backend service.
     *
     * @return A [Response] containing the service status or related information.
     */
    @POST("start-service")
    suspend fun startBackendService(): Response<Map<String, String>>

    /**
     * Retrieves a list of all NFL teams.
     *
     * @return A [Call] to fetch a list of [Team].
     */
    @GET("/teams")
    fun getTeams(): Call<List<Team>>

    /**
     * Retrieves the games for the current year.
     *
     * @return A [Call] to fetch games for the current year in [CurrentYearGames].
     */
    @GET("/games/current-year")
    fun getCurrentYearGames(): Call<CurrentYearGames>

    /**
     * Retrieves all games from all years.
     *
     * @return A [Call] to fetch all games data in [AllYearsGames].
     */
    @GET("/games/all")
    fun getAllYearsGames(): Call<AllYearsGames>

    /**
     * Retrieves games played in a specific year.
     *
     * @param year The year of the games to retrieve.
     * @return A [Call] to fetch the games played in the specified year in [YearGamesResponse].
     */
    @GET("/games/{year}")
    fun getGamesByYear(@Path("year") year: Int): Call<YearGamesResponse>

    /**
     * Clears all data in the backend.
     *
     * @return A [Call] to clear backend data, returning a status map.
     */
    @POST("/clear-data")
    fun clearData(): Call<Map<String, String>>

    /**
     * Simulates the next year's NFL games.
     *
     * @return A [Call] to simulate games for the next year, returning a status map.
     */
    @POST("/simulate-next-year")
    fun simulateNextYear(): Call<Map<String, String>>
}
