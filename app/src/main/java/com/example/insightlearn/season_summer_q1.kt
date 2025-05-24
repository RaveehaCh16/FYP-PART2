package com.example.insightlearn

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.insightlearn.R

class SummerQuestionActivity : AppCompatActivity() {

    private lateinit var option1: Button
    private lateinit var option2: Button
    private lateinit var option3: Button
    private lateinit var option4: Button
    private lateinit var nextButton: Button

    private var answerSelected = false
    private var isCorrect = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.summer_qs)

        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)
        nextButton = findViewById(R.id.nextButton)

        option1.setOnClickListener { handleAnswerSelection(option1, true) }
        option2.setOnClickListener { handleAnswerSelection(option2, false) }
        option3.setOnClickListener { handleAnswerSelection(option3, false) }
        option4.setOnClickListener { handleAnswerSelection(option4, false) }

        nextButton.setOnClickListener {
            if (answerSelected) {
                val intent = Intent(this, SummerQuestionActivity2::class.java)
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
            Toast.makeText(this, "Oops! Wrong Answer, the correct answer is Pink", Toast.LENGTH_SHORT).show()
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
