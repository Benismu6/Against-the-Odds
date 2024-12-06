// AppNavigation.kt
package edu.towson.cosc435.basaran.againsttheodds.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.towson.cosc435.basaran.againsttheodds.ui.screens.BettingOddsCalculatorScreen
import edu.towson.cosc435.basaran.againsttheodds.ui.screens.DashboardScreen
import edu.towson.cosc435.basaran.againsttheodds.ui.screens.DashboardScreen2
import edu.towson.cosc435.basaran.againsttheodds.ui.screens.HelpScreen
import edu.towson.cosc435.basaran.againsttheodds.ui.screens.SettingsScreen
import edu.towson.cosc435.basaran.againsttheodds.ui.screens.StatisticsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") { DashboardScreen2(navController) }
        composable("statistics") { StatisticsScreen(navController) }
        composable("calculator") { BettingOddsCalculatorScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("help") { HelpScreen(navController) }
    }
}