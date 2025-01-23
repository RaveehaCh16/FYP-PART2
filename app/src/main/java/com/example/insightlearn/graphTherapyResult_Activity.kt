package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class graphTherapyResult_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.graphtherapy_result)

        val totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 0)
        val correctCount = intent.getIntExtra("CORRECT_COUNT", 0)
        val incorrectCount = intent.getIntExtra("INCORRECT_COUNT", 0)

        val resultTextView = findViewById<TextView>(R.id.resultTextView)
        val homeButton = findViewById<Button>(R.id.homeButton)
        val settingsButton = findViewById<Button>(R.id.settingsButton)
        resultTextView.text = """
            Total Questions: $totalQuestions
            Correct Answers: $correctCount
            Incorrect Answers: $incorrectCount
        """.trimIndent()

        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        /*
        // Navigate to Settings Screen
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

         */
    }
}
