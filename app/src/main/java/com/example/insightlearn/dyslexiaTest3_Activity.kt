package com.example.insightlearn

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DyslexiaTest3Activity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView
    private lateinit var leftColumn: LinearLayout
    private lateinit var rightColumn: LinearLayout

    private val leftWords = listOf("TREE", "STAR", "HAND", "BOAT", "MOON", "KING", "FROG", "SNOW", "CLOCK", "CHAIR")
    private val rightWords = listOf("KING", "HAND", "BOAT", "CLOCK", "CHAIR", "TREE", "MOON", "FROG", "STAR", "SNOW")

    // Dummy tracking (replace this logic with actual selection tracking)
    private val totalOccurrences = leftWords.size
    private var correctSelections = 0
    private var incorrectSelections = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dyslexiatest3)

        drawingView = findViewById(R.id.drawingArea)
        leftColumn = findViewById(R.id.leftColumn)
        rightColumn = findViewById(R.id.rightColumn)

        populateColumns()

        findViewById<ImageButton>(R.id.homeBtn).setOnClickListener { finishAffinity() }
        findViewById<ImageButton>(R.id.backBtn).setOnClickListener { finish() }
        findViewById<ImageButton>(R.id.nextBtn).setOnClickListener {
            // For now, simulate score (replace with real logic)
            correctSelections = 6
            incorrectSelections = 4
            navigateToResultScreen()
        }
    }

    private fun createWordView(word: String): TextView {
        return TextView(this).apply {
            text = word
            textSize = 22f
            setTextColor(Color.BLACK)
            setPadding(32, 24, 32, 24)
            setBackgroundResource(R.drawable.word_item_background)
            setOnClickListener {
                // TODO: Add logic to select and draw line between words
            }
        }
    }

    private fun populateColumns() {
        leftWords.forEach { word -> leftColumn.addView(createWordView(word)) }
        rightWords.forEach { word -> rightColumn.addView(createWordView(word)) }
    }

    private fun navigateToResultScreen() {
        val intent = Intent(this, dyslexiaResultActivity::class.java).apply {
            putExtra("TOTAL_OCCURRENCES", totalOccurrences)
            putExtra("CORRECT_SELECTIONS", correctSelections)
            putExtra("INCORRECT_SELECTIONS", incorrectSelections)
            putExtra("CURRENT_TEST", 3)
        }

        startActivity(intent)
        finish()
    }
}
