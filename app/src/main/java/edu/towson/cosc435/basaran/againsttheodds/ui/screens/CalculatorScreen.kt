package edu.towson.cosc435.basaran.againsttheodds.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.towson.cosc435.basaran.againsttheodds.ui.components.NavigationBar
import edu.towson.cosc435.basaran.againsttheodds.viewmodels.BettingOddsViewModel

/**
 * Betting Odds Calculator Screen.
 *
 * This screen allows users to:
 * - Select two teams.
 * - Specify the year of the match.
 * - Choose "Over" or "Under" with corresponding amounts.
 * - Calculate confidence levels and get betting advice.
 *
 * @param navController The [NavController] for navigating back to previous screens.
 * @param viewModel The [BettingOddsViewModel] responsible for managing betting-related logic.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BettingOddsCalculatorScreen(
    navController: NavController,
    viewModel: BettingOddsViewModel = viewModel()
) {
    // UI states for user input
    var selectedTeam1 by remember { mutableStateOf("") }
    var selectedTeam2 by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var isOverSelected by remember { mutableStateOf(false) }
    var isUnderSelected by remember { mutableStateOf(false) }
    var amount by remember { mutableStateOf("") }

    // Observing states from ViewModel
    val confidenceLevel by viewModel.confidence.observeAsState(null)
    val explanation by viewModel.explanation.observeAsState("")
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState(null)

    // Main layout
    Scaffold(
        // Top bar with navigation
        topBar = {
            TopAppBar(
                title = { Text("Betting Odds Calculator") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Row 1: Select Team 1
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Select Team 1:")
                TextField(
                    value = selectedTeam1,
                    onValueChange = { selectedTeam1 = it },
                    placeholder = { Text("Team 1") },
                    modifier = Modifier.width(150.dp)
                )
            }

            // Row 2: Select Team 2
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Select Team 2:")
                TextField(
                    value = selectedTeam2,
                    onValueChange = { selectedTeam2 = it },
                    placeholder = { Text("Team 2") },
                    modifier = Modifier.width(150.dp)
                )
            }

            // Row 3: Enter Year
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Year of Match:")
                TextField(
                    value = year,
                    onValueChange = { year = it },
                    placeholder = { Text("YYYY") },
                    modifier = Modifier.width(150.dp)
                )
            }

            // Row 4: Over option
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Over:")
                Checkbox(
                    checked = isOverSelected,
                    onCheckedChange = {
                        isOverSelected = it
                        if (it) isUnderSelected = false // Uncheck "Under" if "Over" is selected
                    }
                )
            }

            // Row 5: Under option
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Under:")
                Checkbox(
                    checked = isUnderSelected,
                    onCheckedChange = {
                        isUnderSelected = it
                        if (it) isOverSelected = false // Uncheck "Over" if "Under" is selected
                    }
                )
            }

            // Row 6: Amount
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Amount Over/Under:")
                TextField(
                    value = amount,
                    onValueChange = { amount = it },
                    placeholder = { Text("Amount") },
                    modifier = Modifier.width(150.dp)
                )
            }

            // Button: Calculate Confidence
            Button(
                onClick = {
                    if (year.isNotBlank() && amount.isNotBlank()) {
                        viewModel.calculateConfidence(
                            team1 = selectedTeam1,
                            team2 = selectedTeam2,
                            year = year.toInt(),
                            overOrUnder = if (isOverSelected) "over" else "under",
                            amount = amount.toDouble()
                        )
                    }
                },
                enabled = !isLoading, // Disable button when loading
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calculate Confidence")
            }

            // Show loading indicator if calculation is in progress
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // Confidence Level and Advice in a Card
            if (confidenceLevel != null || explanation.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        confidenceLevel?.let {
                            Text(
                                text = "Confidence Level: ${"%.2f".format(it)}%",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }



                        if (explanation.isNotBlank()) {
                            Text(
                                text = "Advice: $explanation",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Display Error Message
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Log debugging information
            Log.d("BettingOddsViewModel", "Explanation: $explanation")
            Log.d("BettingOddsViewModel", "Error Message: $errorMessage")
        }
    }
}
