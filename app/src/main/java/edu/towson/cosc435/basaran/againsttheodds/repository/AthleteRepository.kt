/**
 * AthleteRepository.kt
 *
 * This file defines the AthleteRepository object, which is responsible for fetching
 * data from an external API and uploading it to Firebase. It provides functions to
 * fetch a list of NFL athletes and store that data in Firebase Realtime Database.
 *
 * Constants:
 *
 * @property API_URL The URL endpoint to fetch the list of NFL athletes from the ESPN API.
 *
 * Functions:
 *
 * @function fetchAndUploadData(onDataFetched: (List<Athlete>) -> Unit)
 * Initiates an API call to fetch a list of athletes. It uses OkHttp to make
 * an asynchronous network request. On successful response, it parses the
 * JSON data into a list of Athlete objects and uploads them to Firebase.
 * The resulting list of athletes is passed to the provided callback function
 * for further processing.
 *
 * @function uploadDataToFirebase(athletes: List<Athlete>)
 * Uploads the list of Athlete objects to Firebase Realtime Database under the
 * "athletes" node. Each athlete's data is stored using its id as the key.
 */

package edu.towson.cosc435.basaran.againsttheodds.repository

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import edu.towson.cosc435.basaran.againsttheodds.data.Athlete
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

object AthleteRepository {

    private const val API_URL = "https://sports.core.api.espn.com/v2/sports/football/leagues/nfl/athletes?limit=1000&active=true"

    fun fetchAndUploadData(onDataFetched: (List<Athlete>) -> Unit) {
        val client = OkHttpClient()
        val request = Request.Builder().url(API_URL).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("AthleteRepository", "API call failed: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { body ->
                    val json = JSONObject(body.string())
                    val items = json.getJSONArray("items")
                    val athletes = mutableListOf<Athlete>()

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val athleteRef = item.getString("\$ref")
                        val athleteId = athleteRef.substringAfterLast("/").substringBefore("?")

                        // Create Athlete object
                        val athlete = Athlete(
                            id = athleteId,
                            url = athleteRef
                        )
                        athletes.add(athlete)
                    }

                    // Save to Firebase
                    uploadDataToFirebase(athletes)

                    // Return the athlete list through the callback
                    onDataFetched(athletes)
                }
            }
        })
    }

    private fun uploadDataToFirebase(athletes: List<Athlete>) {
        val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("athletes")
        athletes.forEach { athlete ->
            database.child(athlete.id.toString()).setValue(athlete)
        }
    }
}
