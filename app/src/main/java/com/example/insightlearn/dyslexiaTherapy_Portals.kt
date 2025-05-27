package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DyslexiaTherapy_Portals : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dyslexia_therapy_portal) // Make sure the XML file is named activity_main.xml

        val gameButton = findViewById<Button>(R.id.proButton)
        val practiceButton = findViewById<Button>(R.id.memButton)
        val colorButton = findViewById<Button>(R.id.colorButton)
        val backButton = findViewById<Button>(R.id.backButton)
        val nextDayButton= findViewById<Button>(R.id.nextDayButton)
        val seasonButton= findViewById<Button>(R.id.seasonButton)
        gameButton.setOnClickListener {
            val intent = Intent(this, GameScreenActivity::class.java)
            startActivity(intent)
        }

        practiceButton.setOnClickListener {
            val intent = Intent(this, TherapyPracticeActivity::class.java)
            startActivity(intent)
        }
        colorButton.setOnClickListener {
            val intent = Intent(this, color_gamestart::class.java)
            startActivity(intent)
        }
        seasonButton.setOnClickListener {
            val intent = Intent(this, seasonstart::class.java)
            startActivity(intent)
        }
        nextDayButton.setOnClickListener {
            val intent = Intent(this, DayHop_Game::class.java)
            startActivity(intent)
        }
        backButton.setOnClickListener {
            val intent = Intent(this, TherapyActivity::class.java)
            startActivity(intent)
        }
    }
}
