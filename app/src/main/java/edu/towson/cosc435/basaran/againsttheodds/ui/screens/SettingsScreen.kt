package edu.towson.cosc435.basaran.againsttheodds.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.towson.cosc435.basaran.againsttheodds.ui.components.NavigationBar

/**
 * Composable function for the Settings screen.
 *
 * This screen allows users to:
 * - Enable/disable notifications.
 * - Enable/disable dark mode.
 * - Select a default team.
 * - Navigate to the Help/FAQ section.
 *
 * @param navController The [NavController] used for navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    // State for toggling notifications
    var notificationsEnabled by remember { mutableStateOf(false) }

    // State for toggling dark mode
    var darkModeEnabled by remember { mutableStateOf(false) }

    // State for selecting the default team
    var defaultTeam by remember { mutableStateOf("") }

    // Main layout
    Scaffold(
        // Top bar with back navigation
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        // Bottom navigation bar
        bottomBar = { NavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Notifications toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Notifications")
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it } // Update state when toggled
                )
            }

            // Dark mode toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Dark Mode")
                Switch(
                    checked = darkModeEnabled,
                    onCheckedChange = { darkModeEnabled = it } // Update state when toggled
                )
            }

            // Default team dropdown (currently implemented as a TextField)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Default Team")
                TextField(
                    value = defaultTeam,
                    onValueChange = { defaultTeam = it }, // Update state when text changes
                    placeholder = { Text("Select Team") },
                    modifier = Modifier.width(150.dp)
                )
            }

            // Help/FAQ button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Button(
                    onClick = { navController.navigate("help") }, // Navigate to Help/FAQ screen
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Help/FAQ")
                }
            }

            // Spacer to push version information to the bottom
            Spacer(modifier = Modifier.weight(1f))

            // App version information
            Text(
                text = "Version 1.0.0",
                modifier = Modifier.align(Alignment.Start),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
