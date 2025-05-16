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


        gameButton.setOnClickListener {
            val intent = Intent(this, GameScreenActivity::class.java)
            startActivity(intent)
        }

        practiceButton.setOnClickListener {
            val intent = Intent(this, TherapyPracticeActivity::class.java)
            startActivity(intent)
        }
    }
}
