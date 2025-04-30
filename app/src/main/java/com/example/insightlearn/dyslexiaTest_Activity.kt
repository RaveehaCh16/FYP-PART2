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
    private var totalOccurrences = 0
    private var correctSelections = 0
    private var incorrectSelections = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lex_detection_screen)

        // Initialize views
        instructionText = findViewById(R.id.instructionText)
        gridLayout = findViewById(R.id.gridLayout)
        nextButton = findViewById(R.id.nextButton)
        val backButton: Button = findViewById(R.id.backButton)

        // Generate the test
        generateTest()

        // Handle navigation
        nextButton.setOnClickListener {
            navigateToResultScreen()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun generateTest() {
        // Reset counts
        totalOccurrences = 0
        correctSelections = 0
        incorrectSelections = 0

        // Randomly select the target letter
        targetLetter = listOf('b', 'd', 'p', 'q').random()
        instructionText.text = "Select all the $targetLetter's"

        // Generate random letters for the grid
        val letters = MutableList(20) {
            val randomLetter = listOf('b', 'd', 'p', 'q').random()
            if (randomLetter == targetLetter) totalOccurrences++
            randomLetter
        }

        // Ensure at least one target letter is present
        if (totalOccurrences == 0) {
            val randomIndex = Random.nextInt(letters.size)
            letters[randomIndex] = targetLetter
            totalOccurrences++
        }

        // Populate the grid layout
        gridLayout.removeAllViews()
        for (letter in letters) {
            val button = Button(this).apply {
                text = letter.toString()

                // Using ContextCompat to get color resource
                setBackgroundColor(ContextCompat.getColor(this@DyslexiaTestActivity, R.color.alphabetButtonBackground)) // Custom background color
                setTextColor(ContextCompat.getColor(this@DyslexiaTestActivity, R.color.alphabetButtonText)) // Custom text color

                textSize = 18f
                isAllCaps = false
            }
// Set GridLayout.LayoutParams to control button position
            val params = GridLayout.LayoutParams().apply {
                width = GridLayout.LayoutParams.WRAP_CONTENT
                height = GridLayout.LayoutParams.WRAP_CONTENT
                // Optionally, set column and row span for buttons (if required)
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1) // Occupy one column
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1) // Occupy one row
                setMargins(-15, 20, 8, -18) // Set margins between buttons
            }

            button.layoutParams = params
            button.setOnClickListener {
                if (letter == targetLetter) {
                    correctSelections++
                    button.isEnabled = false // Disable after selection
                    button.setBackgroundColor(ContextCompat.getColor(this@DyslexiaTestActivity, R.color.correctSelection)) // Set incorrect selection color
                } else {
                    incorrectSelections++
                    button.isEnabled = false // Disable after selection
                    button.setBackgroundColor(ContextCompat.getColor(this@DyslexiaTestActivity, R.color.incorrectSelection))
                }
            }
            gridLayout.addView(button)
        }
    }

    private fun navigateToResultScreen() {
        // Pass the results to the result screen
        val intent = Intent(this, dyslexiaResultActivity::class.java).apply {
            putExtra("TOTAL_OCCURRENCES", totalOccurrences)
            putExtra("CORRECT_SELECTIONS", correctSelections)
            putExtra("INCORRECT_SELECTIONS", incorrectSelections)
            putExtra("CURRENT_TEST", 1)
        }

        startActivity(intent)
    }
}

