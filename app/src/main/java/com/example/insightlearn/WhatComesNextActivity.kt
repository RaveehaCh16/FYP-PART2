package com.example.insightlearn

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import kotlin.random.Random

class WhatComesNextActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var questionTextView: TextView
    private lateinit var feedbackTextView: TextView
    private lateinit var optionButtons: List<Button>
    private lateinit var backToPortalButton: Button
    private lateinit var backButton: Button

    private val daysOfWeek = listOf(
        "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    )
    private lateinit var shuffledDays: MutableList<String>
    private var currentIndex = 0
    private var correctAnswer = ""

    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_what_comes_next)

        tts = TextToSpeech(this, this)

        questionTextView = findViewById(R.id.questionTextView)
        feedbackTextView = findViewById(R.id.feedbackTextView)

        optionButtons = listOf(
            findViewById(R.id.optionButton1),
            findViewById(R.id.optionButton2),
            findViewById(R.id.optionButton3)
        )

        backToPortalButton = findViewById(R.id.backToPortalButton)
        backToPortalButton.visibility = View.GONE
        backToPortalButton.setOnClickListener {
            startActivity(Intent(this, DyslexiaTherapy_Portals::class.java))
            finish()
        }

        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Go back to previous screen
        }

        shuffledDays = daysOfWeek.shuffled().toMutableList()
        setupQuestion()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
            tts.setSpeechRate(0.9f)
        }
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun setupQuestion() {
        if (currentIndex >= shuffledDays.size) {
            questionTextView.text = "Great job! You completed all days!"
            feedbackTextView.text = ""
            optionButtons.forEach { it.visibility = View.GONE }
            backToPortalButton.visibility = View.VISIBLE
            speak("Great job! You completed all days!")
            return
        }

        feedbackTextView.text = ""
        val currentDay = shuffledDays[currentIndex]
        val dayIndex = daysOfWeek.indexOf(currentDay)
        correctAnswer = daysOfWeek[(dayIndex + 1) % daysOfWeek.size]

        val questionText = "Today is $currentDay. What comes next?"
        questionTextView.text = questionText
        speak(questionText)

        val wrongOptions = daysOfWeek.filter { it != correctAnswer }.shuffled().take(2)
        val allOptions = (wrongOptions + correctAnswer).shuffled()

        for (i in optionButtons.indices) {
            optionButtons[i].visibility = View.VISIBLE
            optionButtons[i].text = allOptions[i]
            optionButtons[i].setBackgroundColor(Color.parseColor("#4CAF50")) // green
            optionButtons[i].setTextColor(Color.WHITE)
            optionButtons[i].setOnClickListener {
                checkAnswer(optionButtons[i].text.toString())
            }
        }
    }

    private fun checkAnswer(selected: String) {
        if (selected == correctAnswer) {
            val message = "That's right! $correctAnswer comes next!"
            feedbackTextView.text = "✅ $message"
            feedbackTextView.setTextColor(Color.parseColor("#2E7D32"))
            speak(message)
            currentIndex++
        } else {
            val message = "Oops! Try again."
            feedbackTextView.text = "❌ $message"
            feedbackTextView.setTextColor(Color.RED)
            speak(message)
            return
        }

        feedbackTextView.postDelayed({
            setupQuestion()
        }, 2000)
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
