package com.example.insightlearn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TherapyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.therapy_screen)

        val dyslexiaButton = findViewById<Button>(R.id.dyslexiaButton)
        val dysgraphiaButton = findViewById<Button>(R.id.dysgraphiaButton)
        val homeButton = findViewById<Button>(R.id.homeButton)
        val settingsButton = findViewById<Button>(R.id.settingsButton)

        dyslexiaButton.setOnClickListener {
            val intent = Intent(this, dyslexiaTherapyActivity::class.java)
            startActivity(intent)
        }

        dysgraphiaButton.setOnClickListener {
            val intent = Intent(this, AlphabetLearningActivity::class.java)
            startActivity(intent)
        }

        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}
