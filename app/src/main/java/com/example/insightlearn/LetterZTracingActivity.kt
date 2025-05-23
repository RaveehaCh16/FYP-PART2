package com.example.insightlearn

import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LetterZTracingActivity : AppCompatActivity() {

    private lateinit var drawingView: ZDotDrawingView
    private lateinit var resetButton: Button
    private lateinit var successMessage: TextView
    private lateinit var backButton: Button
    private lateinit var instructionText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_letter_z_tracing)

        // Initialize views
        drawingView = findViewById(R.id.drawingView)
        resetButton = findViewById(R.id.resetButton)
        successMessage = findViewById(R.id.successMessage)
        instructionText = findViewById(R.id.instructionText)
        backButton = findViewById(R.id.backButton)

        // Initial instruction
        instructionText.text = "Step 1: Trace the top horizontal line of 'Z'"
        successMessage.visibility = View.GONE

        // Drawing step completion
        drawingView.stepCompletedListener = { isFinished ->
            runOnUiThread {
                if (isFinished) {
                    instructionText.text = "🎉 Awesome! Letter 'Z' completed!"
                    successMessage.text = "You did a great job!"
                    successMessage.setTextColor(Color.parseColor("#4CAF50")) // Green
                    successMessage.visibility = View.VISIBLE
                } else {
                    successMessage.visibility = View.GONE
                    when (drawingView.currentStep) {
                        1 -> instructionText.text = "Step 2: Trace the diagonal stroke of 'Z'"
                        2 -> instructionText.text = "Step 3: Trace the bottom horizontal line of 'Z'"
                    }
                }
            }
        }

        // Reset the drawing
        resetButton.setOnClickListener {
            drawingView.resetCanvas()
            instructionText.text = "Step 1: Trace the top horizontal line of 'Z'"
            successMessage.visibility = View.GONE
        }

        // Pass touch events to drawing view
        drawingView.setOnTouchListener { _, event: MotionEvent ->
            drawingView.onTouchEvent(event)
            true
        }

        // Back button
        backButton.setOnClickListener {
            finish()
        }
    }
}
