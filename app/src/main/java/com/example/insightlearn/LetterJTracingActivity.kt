package com.example.insightlearn

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LetterJTracingActivity : AppCompatActivity() {

    private lateinit var drawingView: JDotDrawingView
    private lateinit var resetButton: Button
    private lateinit var nextButton: Button
    private lateinit var successMessage: TextView
    private lateinit var backButton: Button
    private lateinit var instructionText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_letter_j_tracing)

        // Initialize views
        drawingView = findViewById(R.id.drawingView)
        resetButton = findViewById(R.id.resetButton)
        nextButton = findViewById(R.id.nextButton)
        successMessage = findViewById(R.id.successMessage)
        instructionText = findViewById(R.id.instructionText)
        backButton = findViewById(R.id.backButton)

        // Set initial instruction
        instructionText.text = "Trace the letter 'J' following the dashed guide."

        // Initially hide the next button and success message
        nextButton.visibility = View.GONE
        successMessage.visibility = View.GONE

        // Reset button clears canvas and resets UI
        resetButton.setOnClickListener {
            drawingView.resetCanvas()
            successMessage.visibility = View.GONE
            instructionText.text = "Trace the letter 'J' following the dashed guide."
            nextButton.visibility = View.GONE
        }

        // Next button checks if user has drawn something and then navigates to LetterKTracingActivity
        nextButton.setOnClickListener {
            if (drawingView.hasDrawingContent()) {
                successMessage.text = "🎉 Awesome! You traced the letter 'J'!"
                successMessage.setTextColor(Color.parseColor("#4CAF50")) // Green
                successMessage.visibility = View.VISIBLE

                // Navigate to LetterKTracingActivity after showing message
                val intent = Intent(this, LetterKTracingActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                successMessage.text = "Please trace the letter 'J' first."
                successMessage.setTextColor(Color.RED)
                successMessage.visibility = View.VISIBLE
            }
        }

        // Show next button as soon as user draws something
        drawingView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (drawingView.hasDrawingContent()) {
                    nextButton.visibility = View.VISIBLE
                }
            }
            false // allow drawingView to handle touch events
        }

        // Back button functionality
        backButton.setOnClickListener {
            onBackPressed()
        }
    }
}
