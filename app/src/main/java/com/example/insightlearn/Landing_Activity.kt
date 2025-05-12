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

        // Optional: Scale the animation (remove if not needed)
        helloAnimation.scaleX = 1.5f
        helloAnimation.scaleY = 1.5f

        // Start Lottie animation immediately
        helloAnimation.playAnimation()

        getStartedButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Optional: Restart animation when activity resumes
        // If you don't need it to restart onResume, you can remove the next line
        helloAnimation.playAnimation()
    }
}
