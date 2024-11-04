/**
 * DataRepository.kt
 *
 * This file defines the DataRepository object, which is responsible for fetching
 * data from an external API and uploading it to Firebase. It provides a
 * function to fetch a list of NFL teams and store that data in Firebase
 * Realtime Database.
 *
 * Constants:
 *
 * @property API_URL The URL endpoint to fetch the list of NFL teams from TheSportsDB API.
 *
 * Functions:
 *
 * @function fetchAndUploadData(onDataFetched: (List<Team>) -> Unit)
 * Initiates an API call to fetch a list of teams. It uses OkHttp to make
 * an asynchronous network request. On successful response, it parses the
 * JSON data into a list of Team objects and uploads them to Firebase.
 * The resulting list of teams is passed to the provided callback function
 * for further processing.
 *
 * @function uploadDataToFirebase(teams: List<Team>)
 * Uploads the list of Team objects to Firebase Realtime Database under the
 * "teams" node. Each team's data is stored using its idTeam as the key.
 */
package edu.towson.cosc435.basaran.againsttheodds.repository

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import edu.towson.cosc435.basaran.againsttheodds.data.Team
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

object TeamRepository {

    private const val API_URL = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=NFL"

    fun fetchAndUploadData(onDataFetched: (List<Team>) -> Unit) {
        val client = OkHttpClient()
        val request = Request.Builder().url(API_URL).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("DataRepository", "API call failed: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { body ->
                    val json = JSONObject(body.string())
                    val teamList = json.getJSONArray("teams")
                    val teams = mutableListOf<Team>()

                    for (i in 0 until teamList.length()) {
                        val teamJson = teamList.getJSONObject(i)
                        val team = Team(
                            idTeam = teamJson.getString("idTeam"),
                            strTeam = teamJson.getString("strTeam"),
                            strTeamShort = teamJson.optString("strTeamShort", null),
                            strTeamBadge = teamJson.optString("strBadge", null),
                            strStadium = teamJson.optString("strStadium", null),
                            strStadiumLocation = teamJson.optString("strLocation", null),
                            intStadiumCapacity = teamJson.optInt("intStadiumCapacity", 0),
                            strWebsite = teamJson.optString("strWebsite", null),
                            strLeague = teamJson.optString("strLeague", null),
                            strDescriptionEN = teamJson.optString("strDescriptionEN", null),
                            intFormedYear = teamJson.optInt("intFormedYear", 0),
                            strCountry = teamJson.optString("strCountry", null)
                        )
                        teams.add(team)
                    }

                    // Save to Firebase
                    uploadDataToFirebase(teams)

                    // Return the team list through the callback
                    onDataFetched(teams)
                }
            }
        })
    }

    private fun uploadDataToFirebase(teams: List<Team>) {
        val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("teams")
        teams.forEach { team ->
            database.child(team.idTeam).setValue(team)
        }
    }
}
