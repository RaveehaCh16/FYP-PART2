package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        // Find views
        val detectionButton = findViewById<Button>(R.id.detectionButton)
        val therapyButton = findViewById<Button>(R.id.therapyButton)
        val settingsIcon = findViewById<Button>(R.id.settingsButton)

        // Set up click listeners
        // Inside HomeActivity.kt
        detectionButton.setOnClickListener {
            val intent = Intent(this, DetectionActivity::class.java)
            startActivity(intent)
        }

        therapyButton.setOnClickListener {
            val intent = Intent(this, TherapyActivity::class.java)
            startActivity(intent)
        }

        settingsIcon.setOnClickListener {
            // Navigate to SettingsActivity (if implemented)
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}
