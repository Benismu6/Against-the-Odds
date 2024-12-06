package edu.towson.cosc435.basaran.againsttheodds.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

@Composable
fun DashboardScreen2(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Against the Odds",
            color = Color.Black,
            fontSize = 48.sp,
            modifier = Modifier.padding(top = 75.dp)
        )
        Text(
            text = "Welcome Bettor!",
            color = Color.Black,
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 25.dp)
        )

        Spacer(modifier = Modifier.height(60.dp))

        Row() {
            // Card for Statistics
            Card(
                modifier = Modifier
                    .size(200.dp)
                    .padding(12.dp)
                    .clickable { navController.navigate("statistics") },
                elevation = CardDefaults.cardElevation()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_stats),
                        contentDescription = "Statistics",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(top = 15.dp)
                    )
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
                    .clickable { navController.navigate("calculator") },
                elevation = CardDefaults.cardElevation()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_calc),
                        contentDescription = "Calculator",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(top = 15.dp)
                    )
                    Text(
                        text = "Calculator",
                        color = Color.Black,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }
        }

        Row() {
            // Card for Settings
            Card(
                modifier = Modifier
                    .size(200.dp)
                    .padding(12.dp)
                    .clickable { navController.navigate("settings") },
                elevation = CardDefaults.cardElevation()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = "Settings",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(top = 15.dp)
                    )
                    Text(
                        text = "Settings",
                        color = Color.Black,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }

            // Card for Extra Button
            Card(
                modifier = Modifier
                    .size(200.dp)
                    .padding(12.dp)
                    .clickable { navController.navigate("help") }, // You can modify the destination
                elevation = CardDefaults.cardElevation()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_stats),
                        contentDescription = "Extra Button",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(top = 15.dp)
                    )
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
