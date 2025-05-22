package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.widget.GridLayout.LayoutParams

class DyslexiaTest2Activity : AppCompatActivity() {

    private lateinit var instructionText: TextView
    private lateinit var questionText: TextView
    private lateinit var optionsLayout: GridLayout
    private lateinit var nextButton: Button
    private lateinit var backButton: Button

    private val questions = listOf(
        Triple("C _ T", listOf('A', 'U', 'O'), "CAT,CUT,COT"),
        Triple("J U _ P", listOf('M'), "JUMP"),
        Triple("S _ N", listOf('U', 'I'), "SUN,SIN"),
        Triple("P E _", listOf('N', 'A'), "PEN,PEA"),
        Triple("H _ T", listOf('A', 'I', 'O', 'U'), "HAT,HIT,HOT,HUT"),
        Triple("M _ L K", listOf('I'), "MILK"),
        Triple("B _ T", listOf('A', 'E', 'I', 'U'), "BAT,BET,BIT,BUT"),
        Triple("M _ P", listOf('A', 'O'), "MAP,MOP"),
        Triple("_ E G", listOf('L', 'B', 'P'), "LEG,BEG,PEG"),
        Triple("T _ E E", listOf('R'), "TREE")
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

        instructionText.text = "Fill in the blank."
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
    }

    private fun loadQuestion() {
        val (incompleteWord, correctLetters, _) = questions[currentIndex]
        currentCorrectLetter = correctLetters.random()

        questionText.text = incompleteWord
        optionsLayout.removeAllViews()

        val wrongOptions = ('A'..'Z').filter { it !in correctLetters }.shuffled().take(3)
        val allOptions = (wrongOptions + currentCorrectLetter).shuffled()

        for (option in allOptions) {
            val button = Button(this).apply {
                text = option.toString()
                textSize = 20f
                background = ContextCompat.getDrawable(this@DyslexiaTest2Activity, R.drawable.round_corners)
                setTextColor(ContextCompat.getColor(this@DyslexiaTest2Activity, android.R.color.white))
                setPadding(8, 8, 8, 8)

                layoutParams = LayoutParams().apply {
                    width = resources.getDimensionPixelSize(R.dimen.option_button_width)
                    height = resources.getDimensionPixelSize(R.dimen.option_button_height)
                    setMargins(16, 16, 16, 16)
                    gravity = Gravity.CENTER
                }

                setOnClickListener {
                    disableAllButtons()
                    answerSelected = true

                    if (option == currentCorrectLetter) {
                        correctAnswers++
                        questionText.text = "Correct!"
                        background = ContextCompat.getDrawable(this@DyslexiaTest2Activity, R.drawable.correct_background)
                    } else {
                        incorrectAnswers++
                        questionText.text = "Incorrect"
                        background = ContextCompat.getDrawable(this@DyslexiaTest2Activity, R.drawable.incorrect_background)
                        highlightCorrectButton()
                    }
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

    private fun highlightCorrectButton() {
        for (i in 0 until optionsLayout.childCount) {
            val b = optionsLayout.getChildAt(i) as Button
            if (b.text.toString() == currentCorrectLetter.toString()) {
                b.background = ContextCompat.getDrawable(this, R.drawable.correct_background)
                break
            }
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
