package edu.towson.cosc435.basaran.againsttheodds.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import edu.towson.cosc435.basaran.againsttheodds.viewmodels.StatisticsViewModel

/**
 * Composable function for displaying NFL statistics.
 *
 * This screen allows users to view team statistics, filter by season, sort by wins/losses/team name,
 * and search for specific teams.
 *
 * @param navController The [NavController] used for navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(navController: NavController) {
    // ViewModel instance to manage data and logic
    val viewModel: StatisticsViewModel = viewModel()

    // Observing LiveData from ViewModel
    val teamStats by viewModel.teamStats.observeAsState(emptyList())
    val availableSeasons by viewModel.availableSeasons.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)

    // Local states for UI
    var searchQuery by remember { mutableStateOf("") }
    var sortOrder by remember { mutableStateOf(SortOrder.NONE) }
    var selectedSeason by remember { mutableStateOf("All Seasons") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var isSearchBarVisible by remember { mutableStateOf(false) }

    // Apply filters and sorting to team stats
    val filteredAndSortedStats = teamStats
        .filter { selectedSeason == "All Seasons" || it.season == selectedSeason }
        .filter { it.team.startsWith(searchQuery, ignoreCase = true) }
        .let { stats ->
            when (sortOrder) {
                SortOrder.BY_WINS -> stats.sortedByDescending { it.wins }
                SortOrder.BY_LOSSES -> stats.sortedByDescending { it.losses }
                SortOrder.BY_TEAM_NAME -> stats.sortedBy { it.team }
                SortOrder.NONE -> stats
            }
        }

    // Fetch initial data when the screen is loaded
    LaunchedEffect(Unit) {
        viewModel.fetchTeamStats()
        viewModel.fetchAvailableSeasons()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NFL Statistics") },
                navigationIcon = {
                    // Back button to navigate to the previous screen
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Search bar toggle
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
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Dropdown menu for selecting seasons
                Button(
                    onClick = { isDropdownExpanded = !isDropdownExpanded },
                    modifier = Modifier
                        .width(170.dp)
                        .heightIn(min = 48.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = selectedSeason)
                        Icon(
                            imageVector = if (isDropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isDropdownExpanded) "Collapse dropdown" else "Expand dropdown"
                        )
                    }
                }
                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    Box(
                        modifier = Modifier
                            .heightIn(max = 200.dp)
                            .width(170.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Column {
                            DropdownMenuItem(
                                onClick = {
                                    if (selectedSeason != "All Seasons") {
                                        selectedSeason = "All Seasons"
                                        viewModel.fetchTeamStats(null)
                                    }
                                    isDropdownExpanded = false
                                },
                                text = { Text("All Seasons") }
                            )
                            availableSeasons.forEach { season ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedSeason = season
                                        isDropdownExpanded = false
                                        viewModel.fetchTeamStats(season)
                                    },
                                    text = { Text(season) }
                                )
                            }
                        }
                    }
                }
            }

            // Sorting options
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { sortOrder = SortOrder.BY_TEAM_NAME }) { Text("Name") }
                Button(onClick = { sortOrder = SortOrder.BY_WINS }) { Text("Wins") }
                Button(onClick = { sortOrder = SortOrder.BY_LOSSES }) { Text("Losses") }
                Button(onClick = { sortOrder = SortOrder.NONE }) { Text("Clear Sort") }
            }

            // Display team statistics
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .heightIn(min = 500.dp)
            ) {
                if (isLoading) {
                    // Show a loading spinner when data is being fetched
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        // Table header
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
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Losses",
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                            HorizontalDivider(
                                thickness = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        // Table rows for team stats
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
    }
}

/**
 * Enum class for defining sorting orders.
 */
enum class SortOrder {
    BY_WINS,
    BY_LOSSES,
    BY_TEAM_NAME,
    NONE
}
