package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class graphia_detect : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.graphia_detection_screen)

        val alphaButton: Button = findViewById(R.id.alphaButton)
        val numButton: Button = findViewById(R.id.numButton)
        val homeButton: Button = findViewById(R.id.homebutton)
        val settingsButton: Button = findViewById(R.id.settingsbutton)

        alphaButton.setOnClickListener {
            val intent = Intent(this, testgraph::class.java)
            startActivity(intent)
        }

        numButton.setOnClickListener {
            val intent = Intent(this, numtestgraph::class.java)
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
