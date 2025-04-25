package com.example.insightlearn

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class dyslexiaResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lex_result_screen)
        val resultText: TextView = findViewById(R.id.resultText)

        // Retrieve the results from the intent
        val totalOccurrences = intent.getIntExtra("TOTAL_OCCURRENCES", 0)
        val correctSelections = intent.getIntExtra("CORRECT_SELECTIONS", 0)
        val incorrectSelections = intent.getIntExtra("INCORRECT_SELECTIONS", 0)

        // Display the results
        resultText.text = """
            Total Occurrences: $totalOccurrences
            Correct Selections: $correctSelections
            Incorrect Selections: $incorrectSelections
        """.trimIndent()
    }
}