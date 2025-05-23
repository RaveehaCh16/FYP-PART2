package com.example.insightlearn

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LetterHTracingActivity : AppCompatActivity() {

    private lateinit var drawingView: HDotDrawingView
    private lateinit var resetButton: Button
    private lateinit var nextButton: Button
    private lateinit var successMessage: TextView
    private lateinit var backButton: Button
    private lateinit var instructionText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_letter_h_tracing)

        // Initialize views
        drawingView = findViewById(R.id.drawingView)
        resetButton = findViewById(R.id.resetButton)
        nextButton = findViewById(R.id.nextButton)
        successMessage = findViewById(R.id.successMessage)
        instructionText = findViewById(R.id.instructionText)
        backButton = findViewById(R.id.backButton)

        // Initial instruction
        instructionText.text = "Step 1: Trace the vertical line on the left of 'H'"
        nextButton.visibility = View.GONE
        successMessage.visibility = View.GONE

        // Listener for drawing step completion
        drawingView.stepCompletedListener = { isFinished ->
            runOnUiThread {
                if (isFinished) {
                    instructionText.text = "🎉 Awesome! Letter 'H' completed!"
                    successMessage.text = "You did a great job!"
                    successMessage.setTextColor(Color.parseColor("#4CAF50")) // Green
                    successMessage.visibility = View.VISIBLE
                    nextButton.visibility = View.VISIBLE
                    nextButton.text = "Next"
                } else {
                    successMessage.visibility = View.GONE
                    nextButton.visibility = View.GONE
                    when (drawingView.currentStep) {
                        1 -> instructionText.text = "Step 2: Trace the vertical line on the right of 'H'"
                        2 -> instructionText.text = "Step 3: Trace the horizontal line connecting the two vertical lines of 'H'"
                    }
                }
            }
        }

        // Reset the drawing
        resetButton.setOnClickListener {
            drawingView.resetCanvas()
            successMessage.visibility = View.GONE
            instructionText.text = "Step 1: Trace the vertical line on the left of 'H'"
            nextButton.visibility = View.GONE
        }

        // Next button navigates to Letter I if completed
        nextButton.setOnClickListener {
            if (drawingView.isCompleted()) {
                val intent = Intent(this, LetterITracingActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                successMessage.text = "Please complete all steps!"
                successMessage.setTextColor(Color.RED)
                successMessage.visibility = View.VISIBLE
            }
        }

        // Back button goes back
        backButton.setOnClickListener {
            finish()
        }
    }
}
