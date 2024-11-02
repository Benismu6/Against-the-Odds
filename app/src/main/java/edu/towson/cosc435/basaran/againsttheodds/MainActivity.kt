/**
 * MainActivity.kt
 *
 * This file defines the MainActivity, which serves as the main entry point of the app.
 * It sets up the navigation between different fragments such as Dashboard, Statistics,
 * Betting Calculator, and Settings.
 *
 * Responsibilities:
 * - Initializes the main user interface and navigation
 * - Connects to Firebase for potential data retrieval and storage
 *
 * Key Methods:
 * - onCreate: Sets up the content and initializes Firebase
 */

package edu.towson.cosc435.basaran.againsttheodds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.towson.cosc435.basaran.againsttheodds.models.TeamViewModel
import edu.towson.cosc435.basaran.againsttheodds.ui.theme.AgainstTheOddsTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: TeamViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgainstTheOddsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // You may call your composables here
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(TeamViewModel::class.java)

        // Observe the list of team names and print them
        viewModel.teamList.observe(this, Observer { teams ->
            teams.forEach { team ->
                println("Team name: ${team.strTeam}")
            }
        })

        // Start the data fetch and upload process
        viewModel.fetchAndUploadData()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

