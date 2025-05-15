package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class dyslexiaResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lex_result_screen)

        // Get values passed from test activities
        val totalAttempts = intent.getIntExtra("TOTAL_OCCURRENCES", -1)
        val correctAnswers = intent.getIntExtra("CORRECT_SELECTIONS", -1)
        val incorrectAnswers = intent.getIntExtra("INCORRECT_SELECTIONS", -1)

        val totalAttemptsAlt = intent.getIntExtra("TOTAL_ATTEMPTS", -1)
        val correctAnswersAlt = intent.getIntExtra("CORRECT_ANSWERS", -1)
        val incorrectAnswersAlt = intent.getIntExtra("INCORRECT_ANSWERS", -1)

        val currentTest = intent.getIntExtra("CURRENT_TEST", 1)

        val resultText = findViewById<TextView>(R.id.resultText)
        val nextTestButton = findViewById<Button>(R.id.nextTestButton)
        val resultContainer = findViewById<LinearLayout>(R.id.resultContainer)

        val finalTotal = if (totalAttempts != -1) totalAttempts else totalAttemptsAlt
        val finalCorrect = if (correctAnswers != -1) correctAnswers else correctAnswersAlt
        val finalIncorrect = if (incorrectAnswers != -1) incorrectAnswers else incorrectAnswersAlt

        val resultMessage = """
            Total Attempts: $finalTotal
            Correct Answers: $finalCorrect
            Incorrect Answers: $finalIncorrect
        """.trimIndent()

        resultText.text = resultMessage

        when (currentTest) {
            1 -> {
                nextTestButton.visibility = View.VISIBLE
                nextTestButton.text = "Go to Test 2"
                nextTestButton.setOnClickListener {
                    startActivity(Intent(this, DyslexiaTest2Activity::class.java))
                    finish()
                }
            }
            2 -> {
                nextTestButton.visibility = View.VISIBLE
                nextTestButton.text = "Go to Test 3"
                nextTestButton.setOnClickListener {
                    startActivity(Intent(this, DyslexiaTest3Activity::class.java))
                    finish()
                }
            }
            3 -> {
                nextTestButton.visibility = View.GONE

                // ✅ Add BACK TO PORTAL button for Test 3
                val backToPortalBtn = Button(this).apply {
                    text = "BACK TO PORTAL"
                    setBackgroundResource(R.drawable.round_corners)
                    setPadding(40, 20, 40, 20)
                    setTextColor(ContextCompat.getColor(context, android.R.color.black))
                    textSize = 16f
                    setOnClickListener {
                        startActivity(Intent(this@dyslexiaResultActivity, lex_detect_types::class.java))
                        finish()
                    }
                }
                resultContainer.addView(backToPortalBtn)
            }
        }
    }
}
