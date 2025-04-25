package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class lex_detect_types : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dyslexia_detection_screen) // Make sure the XML file is named activity_main.xml

        val speechButton = findViewById<Button>(R.id.proButton)
        val memoryButton = findViewById<Button>(R.id.memButton)

/*
        speechButton.setOnClickListener {
            val intent = Intent(this, SpeechTestActivity::class.java)
            startActivity(intent)
        }

        memoryButton.setOnClickListener {
            val intent = Intent(this, MemoryTestActivity::class.java)
            startActivity(intent)
        }*/
    }
}
