package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.insightlearn.R

class GameScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_screen)

        // Handle Start Button Click
        val startButton = findViewById<LottieAnimationView>(R.id.startButtonAnimation)
        startButton.setOnClickListener {
            val intent = Intent(this, SpeechBingoActivity::class.java)
            startActivity(intent)
        }
    }
}
