package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class DyslexiaTest3Activity : AppCompatActivity() {

    private lateinit var leftColumn: LinearLayout
    private lateinit var rightColumn: LinearLayout

    private val leftWords = listOf("Tree", "Star", "Hand", "Boat", "Moon", "King", "Frog", "Snow", "Clock", "Chair")
    private val rightWords = listOf("King", "Hand", "Boat", "Clock", "Chair", "Tree", "Moon", "Frog", "Star", "Snow")

    private var selectedLeft: TextView? = null
    private var selectedRight: TextView? = null

    private var correctAnswers = 0
    private var incorrectAnswers = 0
    private var totalAttempts = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dyslexiatest3)

        leftColumn = findViewById(R.id.leftColumn)
        rightColumn = findViewById(R.id.rightColumn)

        setupColumns()

        findViewById<Button>(R.id.backBtn).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.nextBtn).setOnClickListener {
            navigateToResultScreen()
        }
    }

    private fun setupColumns() {
        leftColumn.removeAllViews()
        rightColumn.removeAllViews()

        for (word in leftWords) {
            leftColumn.addView(createWordTextView(word, isLeft = true))
        }
        for (word in rightWords) {
            rightColumn.addView(createWordTextView(word, isLeft = false))
        }
    }

    private fun createWordTextView(text: String, isLeft: Boolean): TextView {
        val tv = TextView(this)
        tv.text = text
        tv.textSize = 18f
        tv.setPadding(16, 16, 16, 16)
        tv.gravity = android.view.Gravity.CENTER
        tv.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        tv.setBackgroundResource(R.drawable.word_item_background)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(8, 12, 8, 12)
        tv.layoutParams = params

        tv.setOnClickListener {
            if (!tv.isClickable) return@setOnClickListener

            if (isLeft) {
                selectedLeft?.background = ContextCompat.getDrawable(this, R.drawable.word_item_background)
                selectedLeft = tv
                tv.setBackgroundResource(R.drawable.word_item_selected)
            } else {
                selectedRight?.background = ContextCompat.getDrawable(this, R.drawable.word_item_background)
                selectedRight = tv
                tv.setBackgroundResource(R.drawable.word_item_selected)
            }

            if (selectedLeft != null && selectedRight != null) {
                checkMatch()
            }
        }

        return tv
    }

    private fun checkMatch() {
        val leftText = selectedLeft?.text.toString()
        val rightText = selectedRight?.text.toString()

        val isCorrect = (leftText == rightText)

        if (isCorrect) {
            correctAnswers++
            selectedLeft?.setBackgroundResource(R.drawable.correct_background)
            selectedRight?.setBackgroundResource(R.drawable.correct_background)
        } else {
            incorrectAnswers++
            selectedLeft?.setBackgroundResource(R.drawable.incorrect_background)
            selectedRight?.setBackgroundResource(R.drawable.incorrect_background)
        }

        // Immediately disable the selected words
        selectedLeft?.isClickable = false
        selectedRight?.isClickable = false
        selectedLeft?.alpha = 0.5f
        selectedRight?.alpha = 0.5f

        // Update total attempts properly
        totalAttempts = correctAnswers + incorrectAnswers

        // Clear selected for next attempt
        selectedLeft = null
        selectedRight = null
    }

    private fun navigateToResultScreen() {
        val intent = Intent(this, dyslexiaResultActivity::class.java).apply {
            putExtra("TOTAL_ATTEMPTS", totalAttempts)
            putExtra("CORRECT_ANSWERS", correctAnswers)
            putExtra("INCORRECT_ANSWERS", incorrectAnswers)
            putExtra("CURRENT_TEST", 3)
        }
        startActivity(intent)
        finish()
    }
}
