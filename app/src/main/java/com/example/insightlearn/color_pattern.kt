package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class color_pattern : AppCompatActivity() {

    private lateinit var buttons: List<Button>
    private lateinit var introLayout: RelativeLayout
    private lateinit var introRoundText: TextView
    private val handler = Handler(Looper.getMainLooper())

    private var pattern = mutableListOf<Int>()
    private var userInput = mutableListOf<Int>()
    private var isUserTurn = false
    private var round = 1
    private val maxRounds = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.color_pattern)

        buttons = listOf(
            findViewById(R.id.redButton),
            findViewById(R.id.blueButton),
            findViewById(R.id.greenButton),
            findViewById(R.id.yellowButton)
        )

        introLayout = findViewById(R.id.roundIntroLayout)
        introRoundText = findViewById(R.id.introRoundText)

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                if (isUserTurn) handleUserInput(index)
            }
        }

        showRoundIntro(round)  // Start by showing round 1 intro
    }

    private fun startRound() {
        if (round > maxRounds) {
            Toast.makeText(this, "Game completed!", Toast.LENGTH_LONG).show()
            return
        }

        isUserTurn = false
        userInput.clear()

        // Add next color in pattern and show pattern after round intro
        val nextColor = (0..3).random()
        pattern.add(nextColor)
        showPattern()
    }

    private fun showRoundIntro(round: Int) {
        introRoundText.text = "$round"
        introLayout.visibility = View.VISIBLE

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        introLayout.startAnimation(fadeIn)

        handler.postDelayed({
            introLayout.startAnimation(fadeOut)
            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    introLayout.visibility = View.GONE
                    startRound()
                }
            })
        }, 1500) // Show intro for 1.5 seconds before fade out
    }

    private fun showPattern() {
        var index = 0
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (index < pattern.size) {
                    flashButton(buttons[pattern[index]])
                    index++
                    handler.postDelayed(this, 800)
                } else {
                    isUserTurn = true
                }
            }
        }, 800)
    }

    private fun flashButton(button: Button) {
        button.alpha = 0.3f
        handler.postDelayed({ button.alpha = 1.0f }, 300)
    }

    private fun handleUserInput(index: Int) {
        userInput.add(index)
        flashButton(buttons[index])
        if (!pattern.startsWith(userInput)) {
            Toast.makeText(this, "Wrong! Try Again.", Toast.LENGTH_SHORT).show()
            userInput.clear()
            isUserTurn = false
            handler.postDelayed({ showPattern() }, 1000)
        } else if (userInput.size == pattern.size) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()

            if (round == maxRounds) {
                val intent = Intent(this, GameEndActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                round++
                handler.postDelayed({ showRoundIntro(round) }, 1000)
            }
        }
    }


    private fun List<Int>.startsWith(other: List<Int>): Boolean {
        if (other.size > this.size) return false
        return this.subList(0, other.size) == other
    }
}
