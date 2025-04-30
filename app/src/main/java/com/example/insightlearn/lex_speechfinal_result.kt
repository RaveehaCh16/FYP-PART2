package com.example.insightlearn

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

class lex_speech_result : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.speechresult)

        // Find the resultTextView and Lottie animation view
        val resultTextView = findViewById<TextView>(R.id.resultTextView)
        val celebrationAnimation = findViewById<LottieAnimationView>(R.id.celebrationAnimation)

        // Check if GlobalTotal.count is not zero to avoid division by zero
        if (GlobalTotal.count > 0) {
            // Calculate the percentage
            val percentage = (GlobalCounter.count.toFloat() / GlobalTotal.count) * 100

            // Determine the level of speech dyslexia based on the percentage
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

            // 🎉 Trigger fireworks animation if result is "Not Dyslexic"
            if (dyslexiaLevel == "Not Dyslexic") {
                celebrate(celebrationAnimation)
            }
        } else {
            // Handle the case where GlobalTotal.count is zero
            resultTextView.text = "Error: Total count is zero."
        }

        // After displaying the result, reset both counters to zero
        GlobalCounter.count = 0
        GlobalTotal.count = 0
    }

    private fun celebrate(celebrationAnimation: LottieAnimationView) {
        // Start the fireworks Lottie animation when the result is 100%
        celebrationAnimation.playAnimation()

        // Optionally, stop the animation after some time (e.g., 5 seconds)
        celebrationAnimation.postDelayed({
            celebrationAnimation.cancelAnimation()
        }, 8000) // Stops after 8 seconds
    }
}
