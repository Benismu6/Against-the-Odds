package edu.towson.cosc435.basaran.againsttheodds.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import edu.towson.cosc435.basaran.againsttheodds.R

/**
 * Dashboard Screen displaying navigation options for the app.
 *
 * This screen contains clickable cards that navigate to other features of the app, such as
 * Statistics, Calculator, Settings, and Help/FAQ.
 *
 * @param navController The [NavController] used for navigating between screens.
 */
@Composable
fun DashboardScreen2(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Title Text
        Text(
            text = "Against the Odds",
            color = Color.Black,
            fontSize = 48.sp,
            modifier = Modifier.padding(top = 75.dp)
        )

        // Subtitle Text
        Text(
            text = "Welcome Bettor!",
            color = Color.Black,
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 25.dp)
        )

        Spacer(modifier = Modifier.height(60.dp))

        // First row with Statistics and Calculator Cards
        Row {
            // Card for Statistics
            Card(
                modifier = Modifier
                    .size(200.dp)
                    .padding(12.dp)
                    .clickable { navController.navigate("statistics") }, // Navigates to Statistics screen
                elevation = CardDefaults.cardElevation()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icon for Statistics
                    Image(
                        painter = painterResource(id = R.drawable.ic_stats),
                        contentDescription = "Statistics",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(top = 15.dp)
                    )
                    // Label for Statistics
                    Text(
                        text = "Statistics",
                        color = Color.Black,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }

            // Card for Betting Odds Calculator
            Card(
                modifier = Modifier
                    .size(200.dp)
                    .padding(12.dp)
                    .clickable { navController.navigate("calculator") }, // Navigates to Calculator screen
                elevation = CardDefaults.cardElevation()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icon for Calculator
                    Image(
                        painter = painterResource(id = R.drawable.ic_calc),
                        contentDescription = "Calculator",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(top = 15.dp)
                    )
                    // Label for Calculator
                    Text(
                        text = "Calculator",
                        color = Color.Black,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }
        }

        // Second row with Settings and Help/FAQ Cards
        Row {
            // Card for Settings
            Card(
                modifier = Modifier
                    .size(200.dp)
                    .padding(12.dp)
                    .clickable { navController.navigate("settings") }, // Navigates to Settings screen
                elevation = CardDefaults.cardElevation()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icon for Settings
                    Image(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = "Settings",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(top = 15.dp)
                    )
                    // Label for Settings
                    Text(
                        text = "Settings",
                        color = Color.Black,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }

            // Card for Help/FAQ
            Card(
                modifier = Modifier
                    .size(200.dp)
                    .padding(12.dp)
                    .clickable { navController.navigate("help") }, // Navigates to Help/FAQ screen
                elevation = CardDefaults.cardElevation()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icon for Help/FAQ
                    Image(
                        painter = painterResource(id = R.drawable.ic_stats), // Reuse of the Statistics icon; replace if necessary
                        contentDescription = "Help/FAQ",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(top = 15.dp)
                    )
                    // Label for Help/FAQ
                    Text(
                        text = "Help/FAQ",
                        color = Color.Black,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(top = 10.dp)
                    )
                }
            }
        }
    }
}
