package com.example.bettingodds

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val team1Spinner: Spinner = findViewById(R.id.team1Spinner)
        val team2Spinner: Spinner = findViewById(R.id.team2Spinner)
        val datePickerInput: EditText = findViewById(R.id.datePickerInput)
        val overOddsInput: EditText = findViewById(R.id.overOddsInput)
        val underOddsInput: EditText = findViewById(R.id.underOddsInput)
        val calculateButton: Button = findViewById(R.id.calculateButton)
        val confidenceLevelTextView: TextView = findViewById(R.id.confidenceLevelTextView)

        // Step 1: Set up the Date Picker
        datePickerInput.setOnClickListener {
            showDatePickerDialog()
        }

        // Step 3: Set up Calculate Button
        calculateButton.setOnClickListener {
            val overOddsText = overOddsInput.text.toString()
            val underOddsText = underOddsInput.text.toString()

            if (overOddsText.isNotEmpty() && underOddsText.isNotEmpty()) {
                val overOdds = overOddsText.toDouble()
                val underOdds = underOddsText.toDouble()

                // Calculate the confidence level based on odds
                val confidenceLevel = calculateConfidenceLevel(overOdds, underOdds)
                confidenceLevelTextView.text = "$confidenceLevel %"
            } else {
                Toast.makeText(this, "Please enter both Over and Under odds", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to show Date Picker Dialog
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            findViewById<EditText>(R.id.datePickerInput).setText(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    // Function to calculate the confidence level based on the "Over" and "Under" odds
    private fun calculateConfidenceLevel(overOdds: Double, underOdds: Double): Double {
        val totalOdds = overOdds + underOdds
        val confidenceLevel = (overOdds / totalOdds) * 100
        return String.format("%.2f", confidenceLevel).toDouble()
    }
}
