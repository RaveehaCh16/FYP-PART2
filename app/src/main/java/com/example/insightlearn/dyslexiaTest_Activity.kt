package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.random.Random

class DyslexiaTestActivity : AppCompatActivity() {

    private lateinit var instructionText: TextView
    private lateinit var gridLayout: GridLayout
    private lateinit var nextButton: Button

    private var targetLetter: Char = 'b'
    private var totalAttempts = 0
    private var correctAnswers = 0
    private var incorrectAnswers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lex_detection_screen)

        instructionText = findViewById(R.id.instructionText)
        gridLayout = findViewById(R.id.gridLayout)
        nextButton = findViewById(R.id.nextButton)
        val backButton: Button = findViewById(R.id.backButton)

        generateTest()

        nextButton.setOnClickListener {
            navigateToResultScreen()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun generateTest() {
        // Reset counts
        totalAttempts = 0
        correctAnswers = 0
        incorrectAnswers = 0

        targetLetter = listOf('b', 'd', 'p', 'q').random()
        instructionText.text = "Select all the $targetLetter's"

        val letters = MutableList(20) {
            listOf('b', 'd', 'p', 'q').random()
        }

        // Ensure at least one target letter
        if (!letters.contains(targetLetter)) {
            val randomIndex = Random.nextInt(letters.size)
            letters[randomIndex] = targetLetter
        }

        gridLayout.removeAllViews()
        for (letter in letters) {
            val button = Button(this).apply {
                text = letter.toString()
                setBackgroundColor(ContextCompat.getColor(this@DyslexiaTestActivity, R.color.alphabetButtonBackground))
                setTextColor(ContextCompat.getColor(this@DyslexiaTestActivity, R.color.alphabetButtonText))
                textSize = 18f
                isAllCaps = false
            }

            val params = GridLayout.LayoutParams().apply {
                width = GridLayout.LayoutParams.WRAP_CONTENT
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1)
                setMargins(-15, 20, 8, -18)
            }

            button.layoutParams = params
            button.setOnClickListener {
                totalAttempts++ // ✅ Always increase on any click

                if (letter == targetLetter) {
                    correctAnswers++
                    button.isEnabled = false
                    button.setBackgroundColor(ContextCompat.getColor(this@DyslexiaTestActivity, R.color.correctSelection))
                } else {
                    incorrectAnswers++
                    button.isEnabled = false
                    button.setBackgroundColor(ContextCompat.getColor(this@DyslexiaTestActivity, R.color.incorrectSelection))
                }
            }
            gridLayout.addView(button)
        }
    }

    private fun navigateToResultScreen() {
        val intent = Intent(this, dyslexiaResultActivity::class.java).apply {
            putExtra("TOTAL_ATTEMPTS", totalAttempts)
            putExtra("CORRECT_ANSWERS", correctAnswers)
            putExtra("INCORRECT_ANSWERS", incorrectAnswers)
            putExtra("CURRENT_TEST", 1)
        }
        startActivity(intent)
    }
}
