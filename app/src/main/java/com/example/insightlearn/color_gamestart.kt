package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class color_gamestart : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.color_pattern_start) // make sure the XML file is named color_game_start.xml

        val getStartedButton = findViewById<Button>(R.id.getStartedOverlay)
        getStartedButton.setOnClickListener {
            val intent = Intent(this, ColorGameRuleActivity::class.java) // replace with your actual next activity
            startActivity(intent)
        }
    }
}
