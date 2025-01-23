package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

class dyslexiaTherapyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lex_therapy)

        // Set onClickListener for the "Next" button
        findViewById<View>(R.id.btn_next).setOnClickListener {
            checkAnswers()
        }
    }

    private fun checkAnswers() {
        var score = 0
        val totalQuestions = 4 // Total number of questions in the activity

        // Define correct answers for each group
        val correctAnswers = mapOf(
            R.id.group1 to R.id.radio_spring_1,
            R.id.group2 to R.id.radio_autumn_2,
            R.id.group3 to R.id.radio_winter_3,
            R.id.group4 to R.id.radio_summer_4
        )

        // Check answers for each group
        for ((groupId, correctId) in correctAnswers) {
            val group = findViewById<RadioGroup>(groupId)
            if (group.checkedRadioButtonId == correctId) {
                score++
            }
        }

        // Navigate to the result screen and pass the results
        val intent = Intent(this, dyslexiaTherapyResultActivity::class.java)
        intent.putExtra("TOTAL_OCCURRENCES", totalQuestions)
        intent.putExtra("CORRECT_SELECTIONS", score)
        intent.putExtra("INCORRECT_SELECTIONS", totalQuestions - score)
        startActivity(intent)
    }
}
