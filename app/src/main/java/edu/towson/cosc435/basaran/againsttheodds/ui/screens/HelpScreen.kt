package edu.towson.cosc435.basaran.againsttheodds.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.towson.cosc435.basaran.againsttheodds.ui.components.NavigationBar

/**
 * HelpScreen provides users with answers to frequently asked questions (FAQ)
 * and contact information for support.
 *
 * @param navController The [NavController] for navigating back to previous screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(navController: NavController) {
    val emails = listOf(
        "zbasara1@students.towson.edu",
        "pkolbe2@students.towson.edu",
        "bmunez1@students.towson.edu",
        "svuchul1@students.towson.edu",
        "kmattox2@students.towson.edu"
    )

    Scaffold(
        // Top app bar with back navigation
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
        // Bottom navigation bar
        bottomBar = { NavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Title
            Text(
                text = "Help/FAQ",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp),
                color = MaterialTheme.colorScheme.primary
            )

            // FAQ section
            FAQContent()

            Spacer(modifier = Modifier.height(24.dp))

            // Contact Emails Section
            Text(
                text = "Contact Support",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.primary
            )

            emails.forEach { email ->
                EmailCard(email = email)
            }
        }
    }
}

/**
 * Composable for FAQ content.
 */
@Composable
fun FAQContent() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FAQItem(
                question = "How do I use the app?",
                answer = "Navigate through the app using the bottom navigation bar. Choose the desired section for statistics, betting odds, or settings."
            )
            FAQItem(
                question = "How do I calculate betting odds?",
                answer = "Go to the 'Betting Odds Calculator' page, select the teams and date, choose Over or Under, enter the corresponding value, and click 'Calculate'."
            )
            FAQItem(
                question = "How do I change app settings?",
                answer = "Go to the 'Settings' page to adjust notifications, dark mode, and select a default team."
            )
        }
    }
}

/**
 * Composable for displaying a single FAQ item.
 *
 * @param question The question text.
 * @param answer The answer text.
 */
@Composable
fun FAQItem(question: String, answer: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = question,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = answer,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * Composable for displaying an email card.
 *
 * @param email The email address to be displayed.
 */
@Composable
fun EmailCard(email: String) {
    val uriHandler = LocalUriHandler.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { uriHandler.openUri("mailto:$email") }, // Opens email client
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = email,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
        }
    }
}
