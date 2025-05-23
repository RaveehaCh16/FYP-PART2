package com.example.insightlearn

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LetterKTracingActivity : AppCompatActivity() {

    private lateinit var drawingView: KDotDrawingView
    private lateinit var resetButton: Button
    private lateinit var nextButton: Button
    private lateinit var successMessage: TextView
    private lateinit var backButton: Button
    private lateinit var instructionText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_letter_k_tracing)

        // Initialize views
        drawingView = findViewById(R.id.drawingView)
        resetButton = findViewById(R.id.resetButton)
        nextButton = findViewById(R.id.nextButton)
        successMessage = findViewById(R.id.successMessage)
        instructionText = findViewById(R.id.instructionText)
        backButton = findViewById(R.id.backButton)

        // Initial state
        instructionText.text = "Step 1: Trace the vertical line of 'K'"
        successMessage.visibility = View.GONE
        nextButton.visibility = View.GONE
        nextButton.text = "Next"  // Change from "Check" to "Next" as in Letter A

        // Step completion listener
        drawingView.stepCompletedListener = { isFinished ->
            runOnUiThread {
                if (isFinished) {
                    instructionText.text = "🎉 Awesome! Letter 'K' completed!"
                    successMessage.text = "You did a great job!"
                    successMessage.setTextColor(Color.parseColor("#4CAF50")) // Green color
                    successMessage.visibility = View.VISIBLE
                    nextButton.visibility = View.VISIBLE
                } else {
                    nextButton.visibility = View.GONE
                    successMessage.visibility = View.GONE
                    when (drawingView.currentStep) {
                        1 -> instructionText.text = "Step 2: Trace the top diagonal line of 'K'"
                        2 -> instructionText.text = "Step 3: Trace the bottom diagonal line of 'K'"
                    }
                }
            }
        }

        // Reset button logic
        resetButton.setOnClickListener {
            drawingView.resetCanvas()
            instructionText.text = "Step 1: Trace the vertical line of 'K'"
            successMessage.visibility = View.GONE
            nextButton.visibility = View.GONE
        }

        // Next button logic (navigate to LetterLTracingActivity)
        nextButton.setOnClickListener {
            if (drawingView.isCompleted()) {
                val intent = Intent(this, LetterLTracingActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                successMessage.text = "Please complete all steps!"
                successMessage.setTextColor(Color.RED)
                successMessage.visibility = View.VISIBLE
            }
        }

        // Back button
        backButton.setOnClickListener {
            finish()
        }
    }
}
