package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class graphDetectionResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.graphresult_screen)

        // Get results from the intent
        val resultMessage = intent.getStringExtra("RESULT_MESSAGE") ?: "No results"
        val totalAnswers = intent.getIntExtra("TOTAL_ANSWERS", 0)
        val correctAnswers = intent.getIntExtra("CORRECT_ANSWERS", 0)
        val incorrectAnswers = intent.getIntExtra("INCORRECT_ANSWERS", 0)

        // Find TextViews and set the data
        val resultTextView = findViewById<TextView>(R.id.resultText)
        resultTextView.text = """
            $resultMessage
            Total Questions: $totalAnswers
            Correct Answers: $correctAnswers
            Incorrect Answers: $incorrectAnswers
        """.trimIndent()

        // Back Button
        val backButton = findViewById<Button>(R.id.backButtonResult)
        backButton.setOnClickListener {
            val intent = Intent(this, graphDetectionActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Home Button
        val homeButton = findViewById<Button>(R.id.homeButton)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
