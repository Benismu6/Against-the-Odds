package edu.towson.cosc435.basaran.againsttheodds

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import edu.towson.cosc435.basaran.againsttheodds.models.TeamViewModel
import edu.towson.cosc435.basaran.againsttheodds.models.StatisticsViewModel
import edu.towson.cosc435.basaran.againsttheodds.ui.theme.AgainstTheOddsTheme

class MainActivity : ComponentActivity() {
    private val statisticsViewModel: StatisticsViewModel by viewModels()
    private val teamViewModel: TeamViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        statisticsViewModel.startService()
        setContent {
            AgainstTheOddsTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(navController = navController, modifier = Modifier.padding(innerPadding))
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
    }

    private fun fetchData() {
        // Start the data fetch and upload process for each ViewModel
        teamViewModel.fetchAndUploadData()
    }
}

@Composable
fun AppNavigation(navController: androidx.navigation.NavController, modifier: Modifier = Modifier) {
    edu.towson.cosc435.basaran.againsttheodds.ui.navigation.AppNavigation()
}
