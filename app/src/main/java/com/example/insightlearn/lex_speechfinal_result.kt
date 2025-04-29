package com.example.insightlearn

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

class lex_speech_result : AppCompatActivity() {

    private val total = 4  // The total number of tests

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.speechresult)

        // Find the resultTextView
        val resultTextView = findViewById<TextView>(R.id.resultTextView)
        val konfettiView = findViewById<KonfettiView>(R.id.konfettiView)

        // Calculate the percentage
        val percentage = (GlobalCounter.count.toFloat() / total) * 100

        // Determine the level of speech dyslexia
        val dyslexiaLevel = when {
            percentage == 100f -> "Not Dyslexic"
            percentage >= 75f -> "Slightly Dyslexic"
            percentage >= 50f -> "Normal Dyslexic"
            percentage >= 25f -> "Highly Dyslexic"
            percentage >= 0f -> "Extreme Dyslexic"
            else -> "Error"
        }

        // Display the result and dyslexia level
        resultTextView.text =
            "Probability: ${"%.2f".format(percentage)}%\n" +
                    "Speech Dyslexia Level: $dyslexiaLevel"
        // 🎉 Trigger celebration if result is "Not Dyslexic"
        if (dyslexiaLevel == "Not Dyslexic") {
            celebrate(konfettiView)
        }
        // After displaying the result, reset the counter
        GlobalCounter.count = 0
    }

    private fun celebrate(konfettiView: KonfettiView) {
        val emitterConfig = Emitter(duration = 2, TimeUnit.SECONDS).perSecond(100)
        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfff44336.toInt(), 0xff4caf50.toInt(), 0xff2196f3.toInt()),
            emitter = emitterConfig,
            position = Position.Relative(0.5, 0.3)
        )
        konfettiView.start(party)
    }
}
