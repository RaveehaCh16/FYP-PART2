package com.example.insightlearn

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LetterVTracingActivity : AppCompatActivity() {

    private lateinit var drawingView: VDotDrawingView
    private lateinit var resetButton: Button
    private lateinit var nextButton: Button
    private lateinit var successMessage: TextView
    private lateinit var backButton: Button
    private lateinit var instructionText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_letter_v_tracing)

        // Bind views
        drawingView = findViewById(R.id.drawingView)
        resetButton = findViewById(R.id.resetButton)
        nextButton = findViewById(R.id.nextButton)
        successMessage = findViewById(R.id.successMessage)
        backButton = findViewById(R.id.backButton)
        instructionText = findViewById(R.id.instructionText)

        // Initial instruction
        instructionText.text = "Step 1: Trace the left slant of 'V'"
        nextButton.visibility = View.GONE
        successMessage.visibility = View.GONE

        // Step completion logic
        drawingView.stepCompletedListener = { isFinished ->
            runOnUiThread {
                if (isFinished) {
                    instructionText.text = "🎉 Great! Letter 'V' completed!"
                    successMessage.text = "You did a great job!"
                    successMessage.setTextColor(Color.parseColor("#4CAF50"))
                    successMessage.visibility = View.VISIBLE
                    nextButton.text = "Next"
                    nextButton.visibility = View.VISIBLE
                } else {
                    nextButton.visibility = View.GONE
                    when (drawingView.currentStep) {
                        1 -> instructionText.text = "Step 2: Trace the right slant of 'V'"
                    }
                }
            }
        }

        // Reset button
        resetButton.setOnClickListener {
            drawingView.resetCanvas()
            instructionText.text = "Step 1: Trace the left slant of 'V'"
            successMessage.visibility = View.GONE
            nextButton.visibility = View.GONE
        }

        // Next button logic
        nextButton.setOnClickListener {
            if (drawingView.isCompleted()) {
                val intent = Intent(this, LetterWTracingActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                successMessage.text = "Please complete all steps!"
                successMessage.setTextColor(Color.RED)
                successMessage.visibility = View.VISIBLE
            }
        }

        // Optional touch pass-through
        drawingView.setOnTouchListener { _, event ->
            drawingView.onTouchEvent(event)
            true
        }

        // Back button logic
        backButton.setOnClickListener {
            finish()
        }
    }
}
