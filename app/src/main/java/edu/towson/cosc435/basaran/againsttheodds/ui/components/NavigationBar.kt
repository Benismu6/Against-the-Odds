package edu.towson.cosc435.basaran.againsttheodds.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

/**
 * A composable function that displays a bottom navigation bar for the app.
 *
 * @param navController The [NavController] used for navigating between screens.
 */
@Composable
fun NavigationBar(navController: NavController) {
    BottomAppBar {
        NavigationBar {

            // Home Navigation Item
            NavigationBarItem(
                selected = false, // Update logic if you need to highlight the selected item
                onClick = { navController.navigate("dashboard") }, // Navigate to the dashboard
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home") }
            )

            // Statistics Navigation Item
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate("statistics") }, // Navigate to statistics
                icon = { Icon(Icons.Default.Info, contentDescription = "Statistics") },
                label = { Text("Statistics") }
            )

            // Calculator Navigation Item
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate("calculator") }, // Navigate to calculator
                icon = { Icon(Icons.Default.Edit, contentDescription = "Calculator") },
                label = { Text("Calculator") }
            )

            // Settings Navigation Item
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate("settings") }, // Navigate to settings
                icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                label = { Text("Settings") }
            )
        }
    }
}
