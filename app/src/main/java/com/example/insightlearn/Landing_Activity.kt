package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landing_screen)

        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val appNameText = findViewById<TextView>(R.id.appNameText)
        val getStartedButton = findViewById<Button>(R.id.getStartedButton)

        // Hide other views initially
        appNameText.alpha = 0f
        getStartedButton.alpha = 0f
        getStartedButton.visibility = Button.INVISIBLE

        // Apply slide up to welcomeText
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        welcomeText.startAnimation(slideUp)

        slideUp.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                appNameText.animate()
                    .alpha(1f)
                    .translationYBy(-20f)
                    .setDuration(1000)
                    .withEndAction {
                        // Make the button visible and fully opaque
                        getStartedButton.visibility = Button.VISIBLE
                        getStartedButton.alpha = 1f

                        // Apply animation
                        val buttonAnim = AnimationUtils.loadAnimation(this@MainActivity, R.anim.button_slide_fade)
                        getStartedButton.startAnimation(buttonAnim)
                    }
                    .start()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })


        getStartedButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}