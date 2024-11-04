package edu.towson.cosc435.basaran.againsttheodds

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import edu.towson.cosc435.basaran.againsttheodds.models.AthleteViewModel
import edu.towson.cosc435.basaran.againsttheodds.models.ScheduleViewModel
import edu.towson.cosc435.basaran.againsttheodds.models.TeamViewModel
import edu.towson.cosc435.basaran.againsttheodds.ui.theme.AgainstTheOddsTheme

class MainActivity : ComponentActivity() {

    private val teamViewModel: TeamViewModel by viewModels()
    private val athleteViewModel: AthleteViewModel by viewModels()
    private val scheduleViewModel: ScheduleViewModel by viewModels()

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

        // Initialize ViewModels and observe data
        setupObservers()
        fetchData()
    }

    private fun setupObservers() {
        // Observe TeamViewModel
        teamViewModel.teamList.observe(this) { teams ->
            if (teams.isNotEmpty()) {
                Log.d("MainActivity", "Team data received: ${teams.size} teams.")
            } else {
                Log.d("MainActivity", "No team data received.")
            }
        }

        // Observe AthleteViewModel
        athleteViewModel.athleteList.observe(this) { athletes ->
            if (athletes.isNotEmpty()) {
                Log.d("MainActivity", "Athlete data received: ${athletes.size} athletes.")
            } else {
                Log.d("MainActivity", "No athlete data received.")
            }
        }

        // Observe ScheduleViewModel
        scheduleViewModel.scheduleData.observe(this) { scheduleResponse ->
            scheduleResponse?.let {
                Log.d("MainActivity", "Schedule data received: $scheduleResponse")
            } ?: run {
                Log.d("MainActivity", "No schedule data received.")
            }
        }

        // Observe error messages
        scheduleViewModel.errorMessage.observe(this) { error ->
            error?.let {
                Log.e("MainActivity", "Error fetching schedule: $it")
            }
        }
    }

    private fun fetchData() {
        // Start the data fetch and upload process for each ViewModel
        teamViewModel.fetchAndUploadData()
        athleteViewModel.fetchAndUploadData()
        scheduleViewModel.fetchAndUploadData()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
