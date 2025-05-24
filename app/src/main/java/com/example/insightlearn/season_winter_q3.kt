package com.yourpackage.yourapp

import AutumnStoryActivity
import WinterStoryActivity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.insightlearn.DyslexiaTherapy_Portals
import com.example.insightlearn.R
import seasonstart

class WinterQuestionActivity3 : AppCompatActivity() {

    private lateinit var option1: Button
    private lateinit var option2: Button
    private lateinit var option3: Button
    private lateinit var option4: Button
    private lateinit var nextButton: Button
    private lateinit var backButton: Button

    private var answerSelected = false
    private var isCorrect = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.winter_qs3)

        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)
        nextButton = findViewById(R.id.nextButton)
        backButton = findViewById(R.id.backButton)

        option1.setOnClickListener { handleAnswerSelection(option1, false) }
        option2.setOnClickListener { handleAnswerSelection(option2, false) }
        option3.setOnClickListener { handleAnswerSelection(option3, true) }
        option4.setOnClickListener { handleAnswerSelection(option4, false) }

        backButton.setOnClickListener{
            val intent= Intent(this, DyslexiaTherapy_Portals:: class.java)
        }
        nextButton.setOnClickListener {
            if (answerSelected) {
                val intent = Intent(this, seasonstart::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select an answer first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleAnswerSelection(selectedButton: Button, correct: Boolean) {
        if (answerSelected) return

        answerSelected = true
        isCorrect = correct

        if (correct) {
            selectedButton.setBackgroundColor(Color.GREEN)
            Toast.makeText(this, "Hurrah! Correct answer", Toast.LENGTH_SHORT).show()
        } else {
            selectedButton.setBackgroundColor(Color.RED)
            option1.setBackgroundColor(Color.GREEN) // Show correct one
            Toast.makeText(this, "Oops! Wrong Answer, the correct answer is black and red", Toast.LENGTH_SHORT).show()
        }

        // Disable all options after selection
        disableAllOptions()
    }

    private fun disableAllOptions() {
        option1.isEnabled = false
        option2.isEnabled = false
        option3.isEnabled = false
        option4.isEnabled = false
    }
}
