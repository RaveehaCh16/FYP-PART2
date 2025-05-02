package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

class MainActivity : AppCompatActivity() {

    private lateinit var helloAnimation: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landing_screen)

        val getStartedButton: Button = findViewById(R.id.getStartedButton)
        helloAnimation = findViewById(R.id.helloAnimation)

        // Scale the animation
        helloAnimation.scaleX = 1.5f
        helloAnimation.scaleY = 1.5f

        // Start the animation from off-screen
        helloAnimation.translationY = 800f

        // Animate from bottom to top slowly
        helloAnimation.animate()
            .translationY(0f)
            .setDuration(500) // slower movement
            .start()

        // Start Lottie animation
        helloAnimation.playAnimation()

        getStartedButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Reset and re-animate when returning to screen
        helloAnimation.translationY = 800f
        helloAnimation.animate()
            .translationY(0f)
            .setDuration(2000)
            .start()

        helloAnimation.playAnimation()
    }
}
