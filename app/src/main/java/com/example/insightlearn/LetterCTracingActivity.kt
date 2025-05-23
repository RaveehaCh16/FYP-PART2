package com.example.insightlearn

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LetterCTracingActivity : AppCompatActivity() {

    private lateinit var drawingView: CDotDrawingView
    private lateinit var resetButton: Button
    private lateinit var nextButton: Button
    private lateinit var successMessage: TextView
    private lateinit var backButton: Button
    private lateinit var instructionText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_letter_c_tracing)

        // Bind views
        drawingView = findViewById(R.id.drawingView)
        resetButton = findViewById(R.id.resetButton)
        nextButton = findViewById(R.id.nextButton)
        successMessage = findViewById(R.id.successMessage)
        backButton = findViewById(R.id.backButton)
        instructionText = findViewById(R.id.instructionText)

        // Initial state
        instructionText.text = "Trace the full letter 'C' along the dotted line"
        nextButton.visibility = View.GONE
        successMessage.visibility = View.GONE

        // Listener for tracing completion
        drawingView.stepCompletedListener = { isFinished ->
            runOnUiThread {
                if (isFinished) {
                    instructionText.text = "🎉 Great! Letter 'C' completed!"
                    successMessage.text = "You did a great job!"
                    successMessage.setTextColor(Color.parseColor("#4CAF50")) // Green
                    successMessage.visibility = View.VISIBLE
                    nextButton.visibility = View.VISIBLE
                } else {
                    instructionText.text = "Try again! Follow the dotted path of 'C'."
                    successMessage.visibility = View.GONE
                    nextButton.visibility = View.GONE
                }
            }
        }

        // Reset button logic
        resetButton.setOnClickListener {
            drawingView.resetCanvas()
            instructionText.text = "Trace the full letter 'C' along the dotted line"
            successMessage.visibility = View.GONE
            nextButton.visibility = View.GONE
        }

        // Next button logic
        nextButton.setOnClickListener {
            if (drawingView.isCompleted()) {
                val intent = Intent(this, LetterDTracingActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                successMessage.text = "⚠️ Please complete the tracing first!"
                successMessage.setTextColor(Color.RED)
                successMessage.visibility = View.VISIBLE
            }
        }

        // Optional: Pass touch events to custom drawing view
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
