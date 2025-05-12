package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView


class GameRuleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_rules)

        // Handle Start Button Click
        val goButton = findViewById<LottieAnimationView>(R.id.goButtonAnimation)
        goButton.setOnClickListener {
            val intent = Intent(this, SpeechBingoActivity::class.java)
            startActivity(intent)
        }
    }
}
