package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class DetectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detection_screen)

        // Initialize Buttons
        val dyslexiaButton = findViewById<Button>(R.id.dyslexiaButton)
        val dysgraphiaButton = findViewById<Button>(R.id.dysgraphiaButton)
        val backButton = findViewById<Button>(R.id.backButton)

        // Navigate to Dyslexia Details
        dyslexiaButton.setOnClickListener {
            val intent = Intent(this, lex_detect_types::class.java)
            startActivity(intent)
        }

        // Navigate to Dysgraphia Details
        dysgraphiaButton.setOnClickListener {
            val intent = Intent(this, testgraph::class.java)
            startActivity(intent)
        }

        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
                }



    }
}
