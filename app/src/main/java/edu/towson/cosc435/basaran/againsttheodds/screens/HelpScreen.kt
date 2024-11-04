// HelpScreen.kt
package edu.towson.cosc435.basaran.againsttheodds.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.towson.cosc435.basaran.againsttheodds.ui.components.NavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help/FAQ") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { NavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Help/FAQ",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Q1: How do I use the app?\nA1: Navigate through the app using the bottom navigation bar. Choose the desired section for statistics, betting odds, or settings.\n\n" +
                        "Q2: How do I calculate betting odds?\nA2: Go to the 'Betting Odds Calculator' page, select the teams and date, choose Over or Under, enter the corresponding value, and click 'Calculate'.\n\n" +
                        "Q3: How do I change app settings?\nA3: Go to the 'Settings' page to adjust notifications, dark mode, and select a default team.\n\n" +
                        "For further assistance, contact support at support@againsttheodds.com.",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}