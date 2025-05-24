package com.example.insightlearn

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class GameEndActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.color_endscreen)

        val backButton = findViewById<Button>(R.id.backButton)
        val replayButton = findViewById<Button>(R.id.replayButton)

        backButton.setOnClickListener {
            // Go back to dyslexia_therapy_portals screen
            val intent = Intent(this, DyslexiaTherapy_Portals::class.java)
            startActivity(intent)
            finish()
        }

        replayButton.setOnClickListener {
            // Restart the color_pattern game
            val intent = Intent(this, color_gamestart ::class.java)
            startActivity(intent)
            finish()
        }
    }
}
