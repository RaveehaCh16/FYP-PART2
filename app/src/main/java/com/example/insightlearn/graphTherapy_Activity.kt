package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class graphTherapyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.graphtherapy)

        val input1 = findViewById<EditText>(R.id.input1)
        val input2 = findViewById<EditText>(R.id.input2)
        val input3 = findViewById<EditText>(R.id.input3)
        val input4 = findViewById<EditText>(R.id.input4)
        val input5 = findViewById<EditText>(R.id.input5)
        val input6 = findViewById<EditText>(R.id.input6)
        val input7 = findViewById<EditText>(R.id.input7)
        val input8 = findViewById<EditText>(R.id.input8)
        val input9 = findViewById<EditText>(R.id.input9)

        val correctAnswers = listOf("AUNT", "SON", "UNCLE", "SISTER", "MOTHER", "FATHER", "BROTHER", "GRANDPA", "GRANDMA")

        val nextButton = findViewById<Button>(R.id.checkAnswersButton)
        nextButton.setOnClickListener {
            val userAnswers = listOf(
                input1.text.toString().uppercase(),
                input2.text.toString().uppercase(),
                input3.text.toString().uppercase(),
                input4.text.toString().uppercase(),
                input5.text.toString().uppercase(),
                input6.text.toString().uppercase(),
                input7.text.toString().uppercase(),
                input8.text.toString().uppercase(),
                input9.text.toString().uppercase()
            )

            val correctCount = userAnswers.filterIndexed { index, answer -> answer == correctAnswers[index] }.size
            val incorrectCount = userAnswers.size - correctCount

            navigateToResultActivity(userAnswers.size, correctCount, incorrectCount)
        }
    }

    private fun navigateToResultActivity(totalQuestions: Int, correctCount: Int, incorrectCount: Int) {
        val intent = Intent(this, graphTherapyResult_Activity::class.java).apply {
            putExtra("TOTAL_QUESTIONS", totalQuestions)
            putExtra("CORRECT_COUNT", correctCount)
            putExtra("INCORRECT_COUNT", incorrectCount)
        }
        startActivity(intent)
    }
}
