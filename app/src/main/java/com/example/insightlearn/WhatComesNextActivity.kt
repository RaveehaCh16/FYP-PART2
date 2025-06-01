package com.example.insightlearn

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.graphics.drawable.GradientDrawable


class WhatComesNextActivity : AppCompatActivity() {

    private lateinit var mainContent: ScrollView
    private lateinit var introLayout: LinearLayout
    private lateinit var daysContainer: LinearLayout

    private lateinit var questionTextView: TextView
    private lateinit var optionButton1: Button
    private lateinit var optionButton2: Button
    private lateinit var optionButton3: Button
    private lateinit var feedbackTextView: TextView
    private lateinit var backToPortalButton: Button

    private val daysOfWeek = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    private val askedIndices = mutableSetOf<Int>()
    private var currentDayIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_what_comes_next)

        introLayout = findViewById(R.id.introLayout)
        daysContainer = findViewById(R.id.daysContainer)
        mainContent = findViewById(R.id.mainContent)

        questionTextView = findViewById(R.id.questionTextView)
        optionButton1 = findViewById(R.id.optionButton1)
        optionButton2 = findViewById(R.id.optionButton2)
        optionButton3 = findViewById(R.id.optionButton3)
        feedbackTextView = findViewById(R.id.feedbackTextView)
        backToPortalButton = findViewById(R.id.backToPortalButton)

        displayDaysSequence()

        Handler(Looper.getMainLooper()).postDelayed({
            introLayout.visibility = View.GONE
            mainContent.visibility = View.VISIBLE
            showNextQuestion()
        }, 10000)

        optionButton1.setOnClickListener { checkAnswer(optionButton1.text.toString()) }
        optionButton2.setOnClickListener { checkAnswer(optionButton2.text.toString()) }
        optionButton3.setOnClickListener { checkAnswer(optionButton3.text.toString()) }

        backToPortalButton.setOnClickListener {
            val intent = Intent(this, DyslexiaTherapy_Portals::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun displayDaysSequence() {
        val colors = listOf(
            "#FFCDD2",  // light red
            "#F8BBD0",  // pink
            "#E1BEE7",  // purple
            "#D1C4E9",  // deep purple
            "#BBDEFB",  // blue
            "#C8E6C9",  // green
            "#FFF9C4"   // yellow
        )

        daysOfWeek.forEachIndexed { index, day ->
            val dayBox = TextView(this).apply {
                text = day
                textSize = 26f
                setTextColor(Color.BLACK)
                setPadding(40, 40, 40, 40)
                textAlignment = TextView.TEXT_ALIGNMENT_CENTER

                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(40, 20, 40, 20)
                }

                // Set colorful background dynamically with rounded corners
                background = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    cornerRadius = 40f
                    setColor(Color.parseColor(colors[index % colors.size]))
                }
            }
            daysContainer.addView(dayBox)
        }
    }



    private fun TextView.setMargins(left: Int, top: Int, right: Int, bottom: Int) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(left, top, right, bottom)
        layoutParams = params
    }

    private fun showNextQuestion() {
        if (askedIndices.size >= daysOfWeek.size - 1) {
            questionTextView.visibility = View.GONE
            optionButton1.visibility = View.GONE
            optionButton2.visibility = View.GONE
            optionButton3.visibility = View.GONE
            feedbackTextView.visibility = View.GONE
            backToPortalButton.visibility = View.VISIBLE
            return
        }

        do {
            currentDayIndex = (0 until daysOfWeek.size - 1).random()
        } while (askedIndices.contains(currentDayIndex))

        askedIndices.add(currentDayIndex)

        val today = daysOfWeek[currentDayIndex]
        val correctAnswer = daysOfWeek[currentDayIndex + 1]

        questionTextView.text = "What comes after $today?"
        feedbackTextView.text = ""

        val options = mutableListOf(correctAnswer)
        while (options.size < 3) {
            val randomDay = daysOfWeek.random()
            if (randomDay != correctAnswer && !options.contains(randomDay)) {
                options.add(randomDay)
            }
        }

        options.shuffle()

        optionButton1.text = options[0]
        optionButton2.text = options[1]
        optionButton3.text = options[2]

        optionButton1.visibility = View.VISIBLE
        optionButton2.visibility = View.VISIBLE
        optionButton3.visibility = View.VISIBLE
        questionTextView.visibility = View.VISIBLE
    }

    private fun checkAnswer(selectedAnswer: String) {
        val correctAnswer = daysOfWeek[currentDayIndex + 1]
        if (selectedAnswer == correctAnswer) {
            feedbackTextView.text = "✅ Correct!"
            Handler(Looper.getMainLooper()).postDelayed({
                showNextQuestion()
            }, 1000)
        } else {
            feedbackTextView.text = "❌ Incorrect!"
        }
    }
}
