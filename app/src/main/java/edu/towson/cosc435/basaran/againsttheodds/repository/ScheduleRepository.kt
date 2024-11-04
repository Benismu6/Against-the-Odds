// ScheduleRepository.kt
package edu.towson.cosc435.basaran.againsttheodds.repository

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import edu.towson.cosc435.basaran.againsttheodds.data.Address
import edu.towson.cosc435.basaran.againsttheodds.data.Competition
import edu.towson.cosc435.basaran.againsttheodds.data.Competitor
import edu.towson.cosc435.basaran.againsttheodds.data.Game
import edu.towson.cosc435.basaran.againsttheodds.data.ScheduleResponse
import edu.towson.cosc435.basaran.againsttheodds.data.ScheduleTeam
import edu.towson.cosc435.basaran.againsttheodds.data.Type
import edu.towson.cosc435.basaran.againsttheodds.data.Venue
import edu.towson.cosc435.basaran.againsttheodds.data.WeekSchedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

object ScheduleRepository {

    private const val BASE_URL = "https://cdn.espn.com/core/nfl/schedule?xhr=1"

    suspend fun fetchAllSchedules(): ScheduleResponse? {
        Log.d("ScheduleRepository", "Full JSON Response: $BASE_URL")
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().url(BASE_URL).build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw Exception("Unexpected response code: ${response.code}")
                }

                response.body?.string()?.let { responseBody ->
                    val json = JSONObject(responseBody)
                    return@withContext parseScheduleFromJson(json)
                }
            }
        }
    }

    private fun parseScheduleFromJson(json: JSONObject): ScheduleResponse {
        val scheduleMap = mutableMapOf<String, WeekSchedule>()

        val scheduleObject = json.optJSONObject("schedule")
        if (scheduleObject != null) {
            scheduleObject.keys().forEach { dateKey ->
                Log.d("ScheduleRepository", "Parsing date key: $dateKey")
                val gamesArray = scheduleObject.optJSONArray(dateKey)
                val gameList = mutableListOf<Game>()

                if (gamesArray != null) {
                    Log.d("ScheduleRepository", "Found games array for date: $dateKey")
                    for (i in 0 until gamesArray.length()) {
                        val gameJson = gamesArray.getJSONObject(i)
                        Log.d("ScheduleRepository", "Parsing game: ${gameJson.optString("uid")}")

                        // Parse individual game details
                        val competitions = mutableListOf<Competition>()
                        val competitionsArray = gameJson.optJSONArray("competitions")

                        if (competitionsArray != null) {
                            for (j in 0 until competitionsArray.length()) {
                                val compJson = competitionsArray.getJSONObject(j)
                                Log.d("ScheduleRepository", "Parsing competition: ${compJson.optString("date")}")

                                val venue = compJson.optJSONObject("venue")?.let {
                                    Venue(
                                        address = it.optJSONObject("address")?.let { addressJson ->
                                            Address(
                                                city = addressJson.optString("city"),
                                                state = addressJson.optString("state")
                                            )
                                        },
                                        fullName = it.optString("fullName"),
                                        indoor = it.optBoolean("indoor"),
                                        id = it.optString("id")
                                    )
                                }

                                val competitors = compJson.optJSONArray("competitors")?.let { teamArray ->
                                    List(teamArray.length()) { tIndex ->
                                        val teamJson = teamArray.getJSONObject(tIndex)
                                        Competitor(
                                            homeAway = teamJson.optString("homeAway"),
                                            score = teamJson.optString("score"),
                                            winner = teamJson.optBoolean("winner"),
                                            team = teamJson.optJSONObject("team")?.let {
                                                ScheduleTeam(
                                                    id = it.optString("id"),
                                                    displayName = it.optString("displayName"),
                                                    abbreviation = it.optString("abbreviation"),
                                                    logo = it.optString("logo")
                                                )
                                            }
                                        )
                                    }
                                }

                                competitions.add(
                                    Competition(
                                        date = compJson.optString("date"),
                                        venue = venue,
                                        competitors = competitors ?: emptyList()
                                    )
                                )
                            }
                        }

                        gameList.add(
                            Game(
                                date = gameJson.optString("date"),
                                uid = gameJson.optString("uid"),
                                competitions = competitions
                            )
                        )
                    }
                } else {
                    Log.d("ScheduleRepository", "No games found for date: $dateKey")
                }

                scheduleMap[dateKey] = WeekSchedule(games = gameList)
            }
        } else {
            Log.d("ScheduleRepository", "No 'schedule' object found in the JSON response.")
        }

        return ScheduleResponse(schedule = scheduleMap)
    }

    fun uploadDataToFirebase(scheduleResponse: ScheduleResponse) {
        val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("schedules")
        scheduleResponse.schedule.forEach { (date, weekSchedule) ->
            weekSchedule.games?.forEach { game ->
                database.child(date).child(game.uid ?: "unknown_uid").setValue(game)
                    .addOnSuccessListener {
                        Log.d("ScheduleRepository", "Game ${game.uid} uploaded successfully.")
                    }
                    .addOnFailureListener { e ->
                        Log.e("ScheduleRepository", "Failed to upload game ${game.uid}: ${e.message}")
                    }
            }
        }
    }
}