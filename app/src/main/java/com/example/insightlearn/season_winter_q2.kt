package com.example.insightlearn

import android.speech.tts.TextToSpeech
import android.widget.TextView
import java.util.*
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.insightlearn.R

class WinterQuestionActivity2 : AppCompatActivity() {

    private lateinit var option1: Button
    private lateinit var option2: Button
    private lateinit var option3: Button
    private lateinit var option4: Button
    private lateinit var nextButton: Button
    private lateinit var tts: TextToSpeech

    private var answerSelected = false
    private var isCorrect = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.winter_q2)

        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)
        nextButton = findViewById(R.id.nextButton)

        option1.setOnClickListener { handleAnswerSelection(option1, false) }
        option2.setOnClickListener { handleAnswerSelection(option2, false) }
        option3.setOnClickListener { handleAnswerSelection(option3, false) }
        option4.setOnClickListener { handleAnswerSelection(option4, true) }

        nextButton.setOnClickListener {
            if (answerSelected) {
                val intent = Intent(this, WinterQuestionActivity3::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select an answer first", Toast.LENGTH_SHORT).show()
            }
        }
        val titleText = findViewById<TextView>(R.id.titleText)

        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
                tts.setSpeechRate(0.5f)  // 👈 0.5x speed
                tts.speak(titleText.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }

    }
    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }

    private fun handleAnswerSelection(selectedButton: Button, correct: Boolean) {
        if (answerSelected) return

        answerSelected = true

        if (correct) {
            selectedButton.text = "✔" // Tick symbol
            Toast.makeText(this, "Hurrah! Correct answer", Toast.LENGTH_SHORT).show()
        } else {
            selectedButton.text = "❌" // Cross symbol
            Toast.makeText(this, "Oops! Wrong answer", Toast.LENGTH_SHORT).show()
        }

        disableAllOptions()
    }

    private fun disableAllOptions() {
        option1.isEnabled = false
        option2.isEnabled = false
        option3.isEnabled = false
        option4.isEnabled = false
    }
}
