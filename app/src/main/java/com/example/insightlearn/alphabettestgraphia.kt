package com.example.insightlearn


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class testgraph : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alphabetdetect)

        // Get the DrawingView by its ID
        val drawingView: DrawingView = findViewById(R.id.drawing)

        // Clear button to reset the drawing
        val clearButton: Button = findViewById(R.id.clearButton)
        clearButton.setOnClickListener {
            drawingView.clearDrawing()
        }

        // SeekBar for adjusting thickness
        val thicknessSeekBar: SeekBar = findViewById(R.id.thicknessSeekBar)
        thicknessSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update stroke width dynamically
                drawingView.setStrokeWidth(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Optional: Do something when the user starts sliding
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Optional: Do something when the user stops sliding
            }
        })

        // Done button to save the drawing
        val doneButton: Button = findViewById(R.id.doneButton) // Make sure you have a Done button in your layout
        doneButton.setOnClickListener {
            val success = drawingView.saveDrawingToPNG(this)
            if (success) {
                // Inform the user that the drawing has been saved
                Toast.makeText(this, "Drawing saved!", Toast.LENGTH_SHORT).show()
            } else {
                // Inform the user that there was an error
                Toast.makeText(this, "Error saving drawing", Toast.LENGTH_SHORT).show()
            }
        }

        // Result button to navigate to abc activity
        val resultButton: Button = findViewById(R.id.resultButton)
        resultButton.setOnClickListener {
            // Create an intent to navigate to the
            val intent = Intent(this, alphabetmodelActivity::class.java)
            startActivity(intent)
        }
    }
}
