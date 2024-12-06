package edu.towson.cosc435.basaran.againsttheodds.api

import edu.towson.cosc435.basaran.againsttheodds.data.TeamDescApiResponse
import retrofit2.Call
import retrofit2.http.GET

/**
 * Service interface for fetching team descriptions from the SportsDB API.
 */
interface TeamDescApiService {

    /**
     * Fetches all NFL teams.
     *
     * @return A [Call] object containing the [TeamApiResponse].
     */
    @GET("api/v1/json/3/search_all_teams.php?l=NFL")
    fun fetchAllTeams(): Call<TeamDescApiResponse>
}
