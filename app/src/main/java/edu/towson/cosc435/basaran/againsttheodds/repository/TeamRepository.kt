package edu.towson.cosc435.basaran.againsttheodds.repository

import android.util.Log
import edu.towson.cosc435.basaran.againsttheodds.data.Team
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object TeamRepository {

    private const val API_URL = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=NFL"

    // PostgreSQL connection details
    private const val DB_URL = "jdbc:postgresql://dpg-ct8o74u8ii6s73cdm6mg-a:5432/teams_gjrs?user=teams_gjrs_user&password=JletSUWzBFb8cCafiZstuLUKp1Q1k7mw"
    private const val DB_USER = "teams_gjrs_user"
    private const val DB_PASSWORD = "JletSUWzBFb8cCafiZstuLUKp1Q1k7mw"

    fun fetchAndUploadData(onDataFetched: (List<Team>) -> Unit) {
        val client = OkHttpClient()
        val request = Request.Builder().url(API_URL).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("TeamRepository", "API call failed: $e")
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

                    // Debugging: Log the team data
                    Log.d("TeamRepository", "Teams received: ${teams.size}")
                    teams.forEach { team ->
                        Log.d("TeamRepository", "Team ID: ${team.idTeam}, Name: ${team.strTeam}")
                    }

                    // Save to PostgreSQL Database
                    uploadDataToPostgres(teams)

                    // Return the team list through the callback
                    onDataFetched(teams)
                }
            }
        })
    }

    private fun uploadDataToPostgres(teams: List<Team>) {
        try {
            // Ensure JDBC driver is loaded
            Class.forName("org.postgresql.Driver")
            Log.d("TeamRepository", "PostgreSQL JDBC Driver loaded successfully.")
        } catch (e: ClassNotFoundException) {
            Log.e("TeamRepository", "PostgreSQL JDBC Driver not found.", e)
            return
        }

        // Log the database URL to check if it is correctly formatted
        Log.d("TeamRepository", "Attempting to connect to PostgreSQL at: $DB_URL")

        try {
            // Establish connection to PostgreSQL
            Log.d("TeamRepository", "Attempting to connect to PostgreSQL...")
            val connection: Connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)
            Log.d("TeamRepository", "Connected to the database!")

            // Prepare the SQL insert statement
            val insertSQL = """
            INSERT INTO teams (idTeam, strTeam, strTeamShort, strTeamBadge, strStadium, 
                               strStadiumLocation, intStadiumCapacity, strWebsite, strLeague, 
                               strDescriptionEN, intFormedYear, strCountry)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """
            val preparedStatement = connection.prepareStatement(insertSQL)

            teams.forEach { team ->
                preparedStatement.setString(1, team.idTeam)
                preparedStatement.setString(2, team.strTeam)
                preparedStatement.setString(3, team.strTeamShort)
                preparedStatement.setString(4, team.strTeamBadge)
                preparedStatement.setString(5, team.strStadium)
                preparedStatement.setString(6, team.strStadiumLocation)
                preparedStatement.setInt(7, team.intStadiumCapacity ?: 0)
                preparedStatement.setString(8, team.strWebsite)
                preparedStatement.setString(9, team.strLeague)
                preparedStatement.setString(10, team.strDescriptionEN)
                preparedStatement.setInt(11, team.intFormedYear ?: 0)
                preparedStatement.setString(12, team.strCountry)

                // Execute the insert and log the result
                preparedStatement.executeUpdate()
                Log.d("TeamRepository", "Inserted team: ${team.strTeam} into the database.")
            }

            preparedStatement.close()
            connection.close()
            Log.d("TeamRepository", "All data successfully uploaded to PostgreSQL!")

        } catch (e: SQLException) {
            Log.e("TeamRepository", "Failed to upload data to PostgreSQL", e)
            e.printStackTrace()  // Print the full stack trace for debugging
        }
    }
}
