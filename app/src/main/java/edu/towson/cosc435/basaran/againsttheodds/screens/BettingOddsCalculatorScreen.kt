// BettingOddsCalculatorScreen.kt
package edu.towson.cosc435.basaran.againsttheodds.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.towson.cosc435.basaran.againsttheodds.ui.components.NavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BettingOddsCalculatorScreen(navController: NavController) {
    var selectedTeam1 by remember { mutableStateOf("") }
    var selectedTeam2 by remember { mutableStateOf("") }
    var dateOfMatch by remember { mutableStateOf("") }
    var overValue by remember { mutableStateOf<Double?>(null) }
    var underValue by remember { mutableStateOf<Double?>(null) }
    var confidenceLevel by remember { mutableStateOf<Double?>(null) }
    var isOverSelected by remember { mutableStateOf(false) }
    var isUnderSelected by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Betting Odds Calculator") },
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

            // Row 3: Date of Match
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Date of Match:")
                TextField(
                    value = dateOfMatch,
                    onValueChange = { dateOfMatch = it },
                    placeholder = { Text("MM/DD/YYYY") },
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
                if (isOverSelected) {
                    TextField(
                        value = overValue?.toString() ?: "",
                        onValueChange = { overValue = it.toDoubleOrNull() },
                        placeholder = { Text("Enter value") },
                        modifier = Modifier.width(150.dp)
                    )
                }
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
                if (isUnderSelected) {
                    TextField(
                        value = underValue?.toString() ?: "",
                        onValueChange = { underValue = it.toDoubleOrNull() },
                        placeholder = { Text("Enter value") },
                        modifier = Modifier.width(150.dp)
                    )
                }
            }

            // Calculate button
            Button(
                onClick = {
                    // Placeholder logic for confidence calculation
                    confidenceLevel = (overValue ?: 0.0 + underValue!! ?: 0.0) / 2 // Simple example
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text("Calculate")
            }

            // Display confidence level
            confidenceLevel?.let {
                Text(
                    text = "Confidence Level: ${"%.2f".format(it)}%",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}