package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class graphDetectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.graph_detection_screen)

        // Inputs
        val inputHen = findViewById<EditText>(R.id.inputHen)
        val inputBag = findViewById<EditText>(R.id.inputBag)
        val inputHat = findViewById<EditText>(R.id.inputHat)
        val inputSun = findViewById<EditText>(R.id.inputSun)

        // Buttons
        val nextButton = findViewById<Button>(R.id.nextButton)
        val backButton = findViewById<Button>(R.id.backButton)

        backButton.setOnClickListener {
            val intent = Intent(this, DetectionActivity::class.java)
            startActivity(intent)
            finish()
        }

        nextButton.setOnClickListener {
            // Check Answers
            val correctAnswers = mapOf(
                "Hen" to "n",
                "Bag" to "a",
                "Hat" to "t",
                "Sun" to "u"
            )

            val userAnswers = mapOf(
                "Hen" to inputHen.text.toString(),
                "Bag" to inputBag.text.toString(),
                "Hat" to inputHat.text.toString(),
                "Sun" to inputSun.text.toString()
            )

            var correct = 0
            var incorrect = 0
            var resultMessage = ""

            // Compare user answers with correct answers
            for ((word, answer) in userAnswers) {
                if (correctAnswers[word] == answer) {
                    correct++
                    resultMessage += "$word: Correct\n"
                } else {
                    incorrect++
                    resultMessage += "$word: Wrong\n"
                }
            }

            val total = correctAnswers.size

            // Navigate to Result Screen
            val intent = Intent(this, graphDetectionResultActivity::class.java)
            intent.putExtra("RESULT_MESSAGE", resultMessage)
            intent.putExtra("TOTAL_ANSWERS", total)
            intent.putExtra("CORRECT_ANSWERS", correct)
            intent.putExtra("INCORRECT_ANSWERS", incorrect)
            startActivity(intent)
        }
    }
}
