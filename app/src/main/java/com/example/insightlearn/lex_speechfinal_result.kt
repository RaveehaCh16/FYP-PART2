package com.example.insightlearn

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class lex_speech_result : AppCompatActivity() {

    private val total = 4  // The total number of tests

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.speechresult)

        // Find the resultTextView
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        // Calculate the percentage
        val percentage = (GlobalCounter.count.toFloat() / total) * 100

        // Determine the level of speech dyslexia
        val dyslexiaLevel = when {
            percentage == 100f -> "Not Dyslexic"
            percentage >= 75f -> "Slightly Dyslexic"
            percentage >= 50f -> "Normal Dyslexic"
            percentage >= 25f -> "Highly Dyslexic"
            percentage >= 0f  -> "Extreme Dyslexic"
            else -> "Error"
        }

        // Display the result and dyslexia level
        resultTextView.text =
            "Probability: ${"%.2f".format(percentage)}%\n" +
                    "Speech Dyslexia Level: $dyslexiaLevel"

        // After displaying the result, reset the counter
        GlobalCounter.count = 0
    }
}
