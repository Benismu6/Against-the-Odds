// NavigationBar.kt
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

@Composable
fun NavigationBar(navController: NavController) {
    BottomAppBar {
        NavigationBar {
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate("dashboard") },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home") }
            )
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate("statistics") },
                icon = { Icon(Icons.Default.Info, contentDescription = "Statistics") },
                label = { Text("Statistics") }
            )
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate("calculator") },
                icon = { Icon(Icons.Default.Edit, contentDescription = "Calculator") },
                label = { Text("Calculator") }
            )
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate("settings") },
                icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                label = { Text("Settings") }
            )
        }
    }
}
