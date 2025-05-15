package com.example.insightlearn


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class testgraph : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alphabetdetect)

        // Step 1: Reference the heading TextView
        val headingText: TextView = findViewById(R.id.headingText)

        // Step 2: Create the list of alphabets
        val alphabetList =
            listOf("B", "C", "D", "E", "F", "J", "K", "L", "P", "R", "Z")

        // Step 3: Pick a random alphabet from the list
        val randomAlphabet = alphabetList.random()

        // Step 4: Update the heading text
        headingText.text = "Write Alphabet $randomAlphabet"

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
                drawingView.setStrokeWidth(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Done/Result button logic
        val resultButton: Button = findViewById(R.id.resultButton)
        resultButton.setOnClickListener {
            val success = drawingView.saveDrawingToPNG(this)
            if (success) {
                Toast.makeText(this, "Drawing saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error saving drawing", Toast.LENGTH_SHORT).show()
            }

            // Navigate to result activity
            val intent = Intent(this, alphabetmodelActivity::class.java)
            startActivity(intent)
        }
    }
}

