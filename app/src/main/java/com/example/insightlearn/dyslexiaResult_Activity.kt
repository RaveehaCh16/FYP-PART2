package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class dyslexiaResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lex_result_screen)

        val totalOccurrences = intent.getIntExtra("TOTAL_OCCURRENCES", 0)
        val correctSelections = intent.getIntExtra("CORRECT_SELECTIONS", 0)
        val incorrectSelections = intent.getIntExtra("INCORRECT_SELECTIONS", 0)
        val currentTest = intent.getIntExtra("CURRENT_TEST", 1)

        val resultText = findViewById<TextView>(R.id.resultText)
        val nextTestButton = findViewById<Button>(R.id.nextTestButton)

        val resultMessage = """
            Total Attempts: $totalOccurrences
            Correct Answers: $correctSelections
            Incorrect Answers: $incorrectSelections
        """.trimIndent()

        resultText.text = resultMessage

        when (currentTest) {
            1 -> {
                nextTestButton.visibility = View.VISIBLE
                nextTestButton.text = "Go to Test 2"
                nextTestButton.setOnClickListener {
                    val intent = Intent(this, DyslexiaTest2Activity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            2 -> {
                nextTestButton.visibility = View.VISIBLE
                nextTestButton.text = "Go to Test 3"
                nextTestButton.setOnClickListener {
                    val intent = Intent(this, DyslexiaTest3Activity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            3 -> {
                nextTestButton.visibility = View.GONE // No further tests
            }
        }
    }
}
