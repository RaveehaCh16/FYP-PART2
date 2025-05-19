package com.example.insightlearn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TherapyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detection_screen)

        // Initialize buttons
        val dyslexiaButton = findViewById<Button>(R.id.dyslexiaButton)
        val dysgraphiaButton = findViewById<Button>(R.id.dysgraphiaButton)
        val backButton = findViewById<Button>(R.id.backButton)


        // Set onClick listeners for buttons
        dyslexiaButton.setOnClickListener {
            val intent = Intent(this, DyslexiaTherapy_Portals ::class.java)
            startActivity(intent)
        }

        dysgraphiaButton.setOnClickListener {
            val intent = Intent(this, youtube_dysgraphia_therapy::class.java)
            startActivity(intent)
        }
        backButton.setOnClickListener {
            val intent = Intent(this, DetectionActivity::class.java)
            startActivity(intent)
        }


    }
}

