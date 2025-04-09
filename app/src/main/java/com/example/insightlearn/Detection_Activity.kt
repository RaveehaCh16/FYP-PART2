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
        val homeButton = findViewById<Button>(R.id.homeButton)
        val settingsButton = findViewById<Button>(R.id.settingsButton)

        // Navigate to Dyslexia Details
        dyslexiaButton.setOnClickListener {
            val intent = Intent(this, dyslexiaTestActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Dysgraphia Details
        dysgraphiaButton.setOnClickListener {
            val intent = Intent(this, graphia_detect::class.java)
            startActivity(intent)
        }

        // Navigate to Home Screen
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Settings Screen
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }


    }
}
