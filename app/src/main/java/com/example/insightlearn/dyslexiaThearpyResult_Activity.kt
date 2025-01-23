package com.example.insightlearn

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class dyslexiaTherapyResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lextherapy_result)

        // Get the values passed from the previous activity
        val totalQuestions = intent.getIntExtra("TOTAL_OCCURRENCES", 0)
        val correctAnswers = intent.getIntExtra("CORRECT_SELECTIONS", 0)
        val incorrectAnswers = intent.getIntExtra("INCORRECT_SELECTIONS", 0)

        // Find the TextView and set the results
        val scoreText: TextView = findViewById(R.id.score_text)
        scoreText.text = """
            Total Questions: $totalQuestions
            Correct Answers: $correctAnswers
            Incorrect Answers: $incorrectAnswers
        """.trimIndent()
    }
}
