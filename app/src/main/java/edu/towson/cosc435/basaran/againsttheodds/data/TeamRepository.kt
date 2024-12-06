package edu.towson.cosc435.basaran.againsttheodds.repository

import android.util.Log
import edu.towson.cosc435.basaran.againsttheodds.api.TeamDescApiInstance
import edu.towson.cosc435.basaran.againsttheodds.data.TeamDescription
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/**
 * Repository for managing team data, including API calls and database interactions.
 */
object TeamRepository {

    // PostgreSQL connection details
    private const val DB_URL = "jdbc:postgresql://dpg-ct8o74u8ii6s73cdm6mg-a:5432/teams_gjrs"
    private const val DB_USER = "teams_gjrs_user"
    private const val DB_PASSWORD = "JletSUWzBFb8cCafiZstuLUKp1Q1k7mw"

    // Coroutine scope for background operations
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    /**
     * Fetches team data from the API and uploads it to the database.
     *
     * @param onDataFetched Callback to handle the list of [TeamDescription].
     */
    fun fetchAndUploadData(onDataFetched: (List<TeamDescription>) -> Unit) {
        repositoryScope.launch {
            try {
                val response = TeamDescApiInstance.api.fetchAllTeams().execute()
                if (response.isSuccessful) {
                    val teams = response.body()?.teams ?: emptyList()
                    Log.d("TeamRepository", "Teams received: ${teams.size}")

                    // Save to PostgreSQL database
                    uploadDataToPostgres(teams)

                    // Pass data to the callback
                    onDataFetched(teams)
                } else {
                    Log.e("TeamRepository", "API response was not successful: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("TeamRepository", "Error fetching or uploading data: ${e.message}", e)
            }
        }
    }

    /**
     * Uploads team data to the PostgreSQL database.
     *
     * @param teams List of [TeamDescription] to upload.
     */
    private fun uploadDataToPostgres(teams: List<TeamDescription>) {
        try {
            Class.forName("org.postgresql.Driver")
            val connection: Connection =
                DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)

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

                preparedStatement.executeUpdate()
                Log.d("TeamRepository", "Inserted team: ${team.strTeam}")
            }

            preparedStatement.close()
            connection.close()
            Log.d("TeamRepository", "All data successfully uploaded to PostgreSQL!")
        } catch (e: SQLException) {
            Log.e("TeamRepository", "Failed to upload data to PostgreSQL", e)
        }
    }
}
