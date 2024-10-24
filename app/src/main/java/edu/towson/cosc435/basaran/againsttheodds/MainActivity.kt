/**
 * MainActivity.kt
 *
 * This file defines the MainActivity, which serves as the main entry point of the app.
 * It sets up the navigation between different fragments such as Dashboard, Statistics,
 * Betting Calculator, and Settings.
 *
 * Responsibilities:
 *
 * Key Methods:
 */

package edu.towson.cosc435.basaran.againsttheodds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import edu.towson.cosc435.basaran.againsttheodds.ui.theme.AgainstTheOddsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgainstTheOddsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AgainstTheOddsTheme {
        Greeting("Android")
    }
}