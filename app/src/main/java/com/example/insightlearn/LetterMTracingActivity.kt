package com.example.insightlearn

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class LetterMTracingActivity : AppCompatActivity() {

    private lateinit var drawingView: MDotDrawingView
    private lateinit var resetButton: Button
    private lateinit var nextButton: Button
    private lateinit var successMessage: TextView
    private lateinit var backButton: Button
    private lateinit var instructionText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_letter_m_tracing)

        // Initialize views
        drawingView = findViewById(R.id.drawingView)
        resetButton = findViewById(R.id.resetButton)
        nextButton = findViewById(R.id.nextButton)
        successMessage = findViewById(R.id.successMessage)
        instructionText = findViewById(R.id.instructionText)
        backButton = findViewById(R.id.backButton)

        // Initial instruction
        instructionText.text = "Step 1: Trace the left vertical line of 'M'"
        nextButton.visibility = View.GONE
        successMessage.visibility = View.GONE

        // Listener for drawing step completion
        drawingView.stepCompletedListener = { isFinished ->
            runOnUiThread {
                if (isFinished) {
                    instructionText.text = "🎉 Awesome! Letter 'M' completed!"
                    successMessage.text = "You did a great job!"
                    successMessage.setTextColor(Color.parseColor("#4CAF50")) // Green
                    successMessage.visibility = View.VISIBLE
                    nextButton.visibility = View.VISIBLE
                } else {
                    nextButton.visibility = View.GONE
                    successMessage.visibility = View.GONE
                    when (drawingView.currentStep) {
                        1 -> instructionText.text = "Step 2: Trace the left diagonal down to middle bottom"
                        2 -> instructionText.text = "Step 3: Trace the right diagonal up to right top"
                        3 -> instructionText.text = "Step 4: Trace the right vertical line"
                    }
                }
            }
        }

        // Reset button logic
        resetButton.setOnClickListener {
            drawingView.resetCanvas()
            instructionText.text = "Step 1: Trace the left vertical line of 'M'"
            successMessage.visibility = View.GONE
            nextButton.visibility = View.GONE
        }

        // Next button logic
        nextButton.setOnClickListener {
            if (drawingView.isCompleted()) {
                Toast.makeText(this, "Great job! Moving to Letter N.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LetterNTracingActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                successMessage.text = "Please complete all steps!"
                successMessage.setTextColor(Color.RED)
                successMessage.visibility = View.VISIBLE
            }
        }

        // Back button logic
        backButton.setOnClickListener {
            finish()
        }
    }
}
