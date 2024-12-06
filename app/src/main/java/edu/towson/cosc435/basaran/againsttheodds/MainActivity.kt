package edu.towson.cosc435.basaran.againsttheodds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import edu.towson.cosc435.basaran.againsttheodds.ui.theme.AgainstTheOddsTheme
import edu.towson.cosc435.basaran.againsttheodds.viewmodels.StatisticsViewModel

/**
 * Main entry point for the application.
 * Responsible for setting up the app's UI, enabling edge-to-edge display, and initializing data services.
 */
class MainActivity : ComponentActivity() {
    // ViewModel for managing statistics and data service interactions
    private val statisticsViewModel: StatisticsViewModel by viewModels()

    /**
     * Lifecycle method called when the activity is created.
     * Initializes the app theme, navigation, and starts any necessary services.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Start the backend service using the StatisticsViewModel
        statisticsViewModel.startService()

        // Set up the Compose UI content
        setContent {
            AgainstTheOddsTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(navController = navController, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

/**
 * Composable function that sets up the app's navigation.
 * Delegates the navigation logic to the AppNavigation component in the navigation package.
 *
 * @param navController The navigation controller to manage app destinations.
 * @param modifier Modifier for styling and layout adjustments.
 */
@Composable
fun AppNavigation(navController: androidx.navigation.NavController, modifier: Modifier = Modifier) {
    // Delegate navigation setup to the navigation package
    edu.towson.cosc435.basaran.againsttheodds.navigation.AppNavigation()
}
