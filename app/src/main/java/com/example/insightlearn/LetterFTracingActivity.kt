package com.example.insightlearn

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LetterFTracingActivity : AppCompatActivity() {

    private lateinit var drawingView: FDotDrawingView
    private lateinit var resetButton: Button
    private lateinit var nextButton: Button
    private lateinit var successMessage: TextView
    private lateinit var backButton: Button
    private lateinit var instructionText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_letter_f_tracing)

        // Bind views
        drawingView = findViewById(R.id.drawingView)
        resetButton = findViewById(R.id.resetButton)
        nextButton = findViewById(R.id.nextButton)
        successMessage = findViewById(R.id.successMessage)
        backButton = findViewById(R.id.backButton)
        instructionText = findViewById(R.id.instructionText)

        // Initial state
        instructionText.text = "Step 1: Trace the vertical line of 'F'"
        nextButton.visibility = View.GONE
        successMessage.visibility = View.GONE

        // Step completion listener
        drawingView.stepCompletedListener = { isFinished ->
            runOnUiThread {
                if (isFinished) {
                    instructionText.text = "🎉 Excellent! Letter 'F' completed!"
                    successMessage.text = "You did a great job!"
                    successMessage.setTextColor(Color.parseColor("#4CAF50")) // Green color like Letter A
                    successMessage.visibility = View.VISIBLE
                    nextButton.visibility = View.VISIBLE
                } else {
                    nextButton.visibility = View.GONE
                    successMessage.visibility = View.GONE
                    when (drawingView.currentStep) {
                        1 -> instructionText.text = "Step 2: Trace the top horizontal line of 'F'"
                        2 -> instructionText.text = "Step 3: Trace the middle horizontal line of 'F'"
                    }
                }
            }
        }

        // Reset button logic
        resetButton.setOnClickListener {
            drawingView.resetCanvas()
            instructionText.text = "Step 1: Trace the vertical line of 'F'"
            successMessage.visibility = View.GONE
            nextButton.visibility = View.GONE
        }

        // Next button logic (navigate to Letter G)
        nextButton.setOnClickListener {
            if (drawingView.isCompleted()) {
                val intent = Intent(this, LetterGTracingActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                successMessage.text = "Please complete all steps!"
                successMessage.setTextColor(Color.RED)
                successMessage.visibility = View.VISIBLE
            }
        }

        // Pass touch events to drawing view
        drawingView.setOnTouchListener { _, event ->
            drawingView.onTouchEvent(event)
            true
        }

        // Back button
        backButton.setOnClickListener {
            finish()
        }
    }
}
