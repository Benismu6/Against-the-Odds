package edu.towson.cosc435.basaran.againsttheodds.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.towson.cosc435.basaran.againsttheodds.ui.screens.BettingOddsCalculatorScreen
import edu.towson.cosc435.basaran.againsttheodds.ui.screens.DashboardScreen2
import edu.towson.cosc435.basaran.againsttheodds.ui.screens.HelpScreen
import edu.towson.cosc435.basaran.againsttheodds.ui.screens.SettingsScreen
import edu.towson.cosc435.basaran.againsttheodds.ui.screens.StatisticsScreen

/**
 * Sets up the navigation structure for the app using Jetpack Compose Navigation.
 *
 * This function defines the navigation graph, connecting routes (destinations) with their
 * respective composable screens.
 */
@Composable
fun AppNavigation() {
    // Remember the NavController to manage app navigation
    val navController = rememberNavController()

    // Define the navigation graph with routes and their associated screens
    NavHost(navController = navController, startDestination = "dashboard") {

        // Route for the main dashboard screen
        composable("dashboard") {
            DashboardScreen2(navController)
        }

        // Route for the statistics screen
        composable("statistics") {
            StatisticsScreen(navController)
        }

        // Route for the betting odds calculator screen
        composable("calculator") {
            BettingOddsCalculatorScreen(navController)
        }

        // Route for the settings screen
        composable("settings") {
            SettingsScreen(navController)
        }

        // Route for the help screen
        composable("help") {
            HelpScreen(navController)
        }
    }
}
