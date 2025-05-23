package com.example.insightlearn

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LetterXTracingActivity : AppCompatActivity() {

    private lateinit var drawingView: XDotDrawingView
    private lateinit var resetButton: Button
    private lateinit var nextButton: Button
    private lateinit var successMessage: TextView
    private lateinit var backButton: Button
    private lateinit var instructionText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_letter_x_tracing)

        drawingView = findViewById(R.id.drawingView)
        resetButton = findViewById(R.id.resetButton)
        nextButton = findViewById(R.id.nextButton)
        successMessage = findViewById(R.id.successMessage)
        backButton = findViewById(R.id.backButton)
        instructionText = findViewById(R.id.instructionText)

        // Initial state
        instructionText.text = "Step 1: Trace the first diagonal of 'X'"
        nextButton.visibility = View.GONE
        successMessage.visibility = View.GONE

        drawingView.stepCompletedListener = { isFinished ->
            runOnUiThread {
                if (isFinished) {
                    instructionText.text = "🎉 Awesome! Letter 'X' completed!"
                    successMessage.text = "You did a great job!"
                    successMessage.setTextColor(Color.parseColor("#4CAF50"))
                    successMessage.visibility = View.VISIBLE
                    nextButton.visibility = View.VISIBLE
                } else {
                    nextButton.visibility = View.GONE
                    if (drawingView.currentStep == 1) {
                        instructionText.text = "Step 2: Trace the second diagonal of 'X'"
                    }
                }
            }
        }

        resetButton.setOnClickListener {
            drawingView.resetCanvas()
            instructionText.text = "Step 1: Trace the first diagonal of 'X'"
            successMessage.visibility = View.GONE
            nextButton.visibility = View.GONE
        }

        nextButton.setOnClickListener {
            if (drawingView.isCompleted()) {
                val intent = Intent(this, LetterYTracingActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                successMessage.text = "Please complete all steps!"
                successMessage.setTextColor(Color.RED)
                successMessage.visibility = View.VISIBLE
            }
        }

        drawingView.setOnTouchListener { _, event ->
            drawingView.onTouchEvent(event)
            true
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}
