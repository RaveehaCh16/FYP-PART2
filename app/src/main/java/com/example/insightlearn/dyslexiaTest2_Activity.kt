package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class DyslexiaTest2Activity : AppCompatActivity() {

    private lateinit var instructionText: TextView
    private lateinit var questionText: TextView
    private lateinit var optionsLayout: GridLayout
    private lateinit var nextButton: Button
    private lateinit var backButton: Button
    private lateinit var homeIcon: ImageView
    private lateinit var settingsIcon: ImageView

    // YOUR ORIGINAL QUESTION LIST WITH MULTIPLE CORRECT OPTIONS
    private val questions = listOf(
        Triple("C _ T", listOf('A', 'U', 'O'), "CAT, CUT, COT"),
        Triple("J U _ P", listOf('M'), "JUMP"),
        Triple("S _ N", listOf('U', 'I'), "SUN, SIN"),
        Triple("P E _", listOf('N', 'A'), "PEN , PEA"),
        Triple("H _ T", listOf('A', 'I', 'O', 'U'), "HAT, HIT, HOT, HUT"),
        Triple("M _ L K", listOf('I'), "MILK"),
        Triple("B _ T", listOf('A', 'E', 'I', 'O', 'U'), "BAT, BET, BIT, BOT, BUT"),
        Triple("M _ P", listOf('A', 'O'), "MAP, MOP"),
        Triple("_ E G", listOf('L', 'B', 'P', 'R'), "LEG, BEG, PEG, REG"),
        Triple("T _ R E", listOf('R'), "TREE")
    )

    private var currentIndex = 0
    private var correctAnswers = 0
    private var incorrectAnswers = 0
    private var answerSelected = false
    private var currentCorrectLetter: Char = ' '

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.missing_letter_test_layout)

        instructionText = findViewById(R.id.instructionText)
        questionText = findViewById(R.id.questionText)
        optionsLayout = findViewById(R.id.optionsLayout)
        nextButton = findViewById(R.id.nextButton)
        backButton = findViewById(R.id.backButton)
        homeIcon = findViewById(R.id.homeIcon)
        settingsIcon = findViewById(R.id.settingsIcon)

        loadQuestion()

        nextButton.setOnClickListener {
            if (!answerSelected) {
                Toast.makeText(this, "Please select an answer first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            currentIndex++
            answerSelected = false

            if (currentIndex >= questions.size) {
                navigateToResultScreen()
            } else {
                loadQuestion()
            }
        }

        backButton.setOnClickListener { finish() }

        homeIcon.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        settingsIcon.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun loadQuestion() {
        val (incompleteWord, correctLetters, correctWords) = questions[currentIndex]

        // Randomly pick one valid letter to act as the correct answer
        currentCorrectLetter = correctLetters.random()

        questionText.text = incompleteWord
        optionsLayout.removeAllViews()

        // Get 3 random incorrect letters
        val wrongOptions = ('A'..'Z')
            .filter { it !in correctLetters }
            .shuffled()
            .take(3)

        // Mix them with the chosen correct letter
        val allOptions = (wrongOptions + currentCorrectLetter).shuffled()

        for (option in allOptions) {
            val button = Button(this).apply {
                text = option.toString()
                setBackgroundColor(ContextCompat.getColor(this@DyslexiaTest2Activity, R.color.alphabetButtonBackground))
                setTextColor(ContextCompat.getColor(this@DyslexiaTest2Activity, R.color.alphabetButtonText))
                textSize = 18f
                isAllCaps = false
                background = ContextCompat.getDrawable(this@DyslexiaTest2Activity, R.drawable.round_corners)
            }

            val params = GridLayout.LayoutParams().apply {
                width = GridLayout.LayoutParams.WRAP_CONTENT
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1)
                setMargins(15, 20, 8, 15)
            }

            button.layoutParams = params

            button.setOnClickListener {
                disableAllButtons()
                answerSelected = true
                val selectedChar = option
                val completedWord = incompleteWord.replace('_', currentCorrectLetter)

                if (selectedChar == currentCorrectLetter) {
                    correctAnswers++
                    questionText.text = "Correct: $completedWord"
                    button.setBackgroundColor(ContextCompat.getColor(this@DyslexiaTest2Activity, R.color.correctSelection))
                } else {
                    incorrectAnswers++
                    questionText.text = "Incorrect.\nCorrect: $completedWord"
                    button.setBackgroundColor(ContextCompat.getColor(this@DyslexiaTest2Activity, R.color.incorrectSelection))
                }
            }

            optionsLayout.addView(button)
        }
    }

    private fun disableAllButtons() {
        for (i in 0 until optionsLayout.childCount) {
            optionsLayout.getChildAt(i).isEnabled = false
        }
    }

    private fun navigateToResultScreen() {
        val intent = Intent(this, dyslexiaResultActivity::class.java).apply {
            putExtra("TOTAL_OCCURRENCES", questions.size)
            putExtra("CORRECT_SELECTIONS", correctAnswers)
            putExtra("INCORRECT_SELECTIONS", incorrectAnswers)
            putExtra("CURRENT_TEST", 2)
        }
        startActivity(intent)
        finish()
    }
}
