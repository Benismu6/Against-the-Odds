// StatisticsScreen.kt
package edu.towson.cosc435.basaran.againsttheodds.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.towson.cosc435.basaran.againsttheodds.ui.components.NavigationBar
import edu.towson.cosc435.basaran.againsttheodds.ui.statistics.StatisticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(navController: NavController) {
    val viewModel: StatisticsViewModel = viewModel()
    val teamStats by viewModel.teamStats.observeAsState(emptyList()) // Correct LiveData handling
    val availableSeasons by viewModel.availableSeasons.observeAsState(emptyList()) // Correct LiveData handling

    // Local states for UI interactivity
    var searchQuery by remember { mutableStateOf("") }
    var sortOrder by remember { mutableStateOf(SortOrder.NONE) }
    var selectedSeason by remember { mutableStateOf("All Seasons") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var isSearchBarVisible by remember { mutableStateOf(false) } // Declare search bar visibility state

    // Filter and sort the team stats
    val filteredAndSortedStats = teamStats
        .filter {
            selectedSeason == "All Seasons" || it.season == selectedSeason
        }
        .filter {
            it.team.startsWith(searchQuery, ignoreCase = true) // Use startsWith instead of contains
        }
        .let { stats ->
            when (sortOrder) {
                SortOrder.BY_WINS -> stats.sortedByDescending { it.wins }
                SortOrder.BY_LOSSES -> stats.sortedByDescending { it.losses }
                SortOrder.BY_TEAM_NAME -> stats.sortedBy { it.team.extractTeamNumber() }
                SortOrder.NONE -> stats
            }
        }


    // Trigger data fetch when the screen loads
    LaunchedEffect(Unit) {
        viewModel.fetchTeamStats()
        viewModel.fetchAvailableSeasons()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NFL Statistics") },
                actions = {
                    if (isSearchBarVisible) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search teams") },
                            modifier = Modifier.width(200.dp),
                            singleLine = true,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    } else {
                        IconButton(onClick = { isSearchBarVisible = true }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                        }
                    }
                }
            )
        },
        bottomBar = { NavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Sort and Filter Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Dropdown Menu for Season Filter
                Button(onClick = { isDropdownExpanded = true }) {
                    Text(selectedSeason)
                }
                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            selectedSeason = "All Seasons"
                            isDropdownExpanded = false
                            viewModel.fetchTeamStats(null) // Fetch all seasons' data
                        },
                        text = { Text(text = "All Seasons") }
                    )

                    availableSeasons.forEach { season ->
                        DropdownMenuItem(
                            onClick = {
                                selectedSeason = season
                                isDropdownExpanded = false
                                viewModel.fetchTeamStats(season) // Fetch data for the selected season
                            },
                            text = { Text(text = season) }
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Text("Sort By:")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { sortOrder = SortOrder.BY_TEAM_NAME }) {
                    Text("Name")
                }
                Button(onClick = { sortOrder = SortOrder.BY_WINS }) {
                    Text("Wins")
                }
                Button(onClick = { sortOrder = SortOrder.BY_LOSSES }) {
                    Text("Losses")
                }
                Button(onClick = { sortOrder = SortOrder.NONE }) {
                    Text("Clear Sort")
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .wrapContentSize(Alignment.Center)
            ) {
                // Table Header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Team",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Wins",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Losses",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.primary)
                }
                // Filtered and Sorted Table Rows
                items(filteredAndSortedStats) { stat ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stat.team,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = stat.wins.toString(),
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = stat.losses.toString(),
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

// Extension Function to Extract Numeric Part of Team Name
fun String.extractTeamNumber(): Int {
    return this.replace(Regex("[^\\d]"), "").toIntOrNull() ?: Int.MAX_VALUE
}

// Sorting Enum
enum class SortOrder {
    BY_WINS,
    BY_LOSSES,
    BY_TEAM_NAME,
    NONE
}
