package com.example.insightlearn

import android.app.Activity
import android.content.Intent
import android.os.*
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class TherapyPracticeActivity : AppCompatActivity() {

    private val words = listOf(
        "ANT", "HAT", "BIN", "BED", "CRY", "HOUR",
        "SHOE", "ROSE", "BELL", "GIRLS", "FIGHT", "SPRAY",
        "CHAIR", "HOUSE", "FLOWER", "AIRPORT", "BROTHER", "JUSTICE",
        "LIVING", "PICTURE", "TELEVISION", "BLACK", "SPOON", "CABLE", "DAUGHTER"
    )

    private var currentIndex = 0
    private lateinit var wordText: TextView
    private lateinit var wordNumber: TextView
    private lateinit var timerText: TextView
    private lateinit var feedbackText: TextView
    private lateinit var nextButton: Button
    private lateinit var backButton: Button

    private val REQUEST_CODE_SPEECH_INPUT = 100
    private var countDownTimer: CountDownTimer? = null
    private var isCurrentWordCorrect = false
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_therapy_practice)

        wordText = findViewById(R.id.wordText)
        wordNumber = findViewById(R.id.wordNumber)
        timerText = findViewById(R.id.timerText)
        feedbackText = findViewById(R.id.feedbackText)
        nextButton = findViewById(R.id.nextButton)
        backButton = findViewById(R.id.backButton)

        feedbackText.visibility = View.GONE
        nextButton.isEnabled = false

        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.ENGLISH
            }
        }

        showWord()

        nextButton.setOnClickListener {
            if (isCurrentWordCorrect) {
                if (currentIndex < words.size - 1) {
                    currentIndex++
                    showWord()
                } else {
                    Toast.makeText(this, "Test complete!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        backButton.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                showWord()
            } else {
                finish() // go back to portal
            }
        }
    }

    private fun showWord() {
        wordText.text = words[currentIndex]
        wordNumber.text = "Word ${currentIndex + 1} of ${words.size}"
        feedbackText.visibility = View.GONE
        nextButton.isEnabled = false
        isCurrentWordCorrect = false

        // Reset timer
        timerText.visibility = View.VISIBLE
        timerText.text = "Time left: 10 s"
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000).toInt()
                timerText.text = "Time left: $seconds s"
            }

            override fun onFinish() {
                wordText.text = ""
                timerText.visibility = View.GONE
                startSpeechToText()
            }
        }.start()
    }

    private fun startSpeechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak the word")
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
        } catch (e: Exception) {
            Toast.makeText(this, "Speech not supported", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == Activity.RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenWord = result?.get(0)?.uppercase(Locale.getDefault()) ?: ""
            val targetWord = words[currentIndex].uppercase(Locale.getDefault())

            if (spokenWord.contains(targetWord)) {
                feedbackText.text = "Correct!"
                isCurrentWordCorrect = true
                nextButton.isEnabled = true
            } else {
                feedbackText.text = "Incorrect. Try again."
                isCurrentWordCorrect = false
                nextButton.isEnabled = false

                tts.speak(targetWord, TextToSpeech.QUEUE_FLUSH, null, null)
                Handler(Looper.getMainLooper()).postDelayed({
                    startSpeechToText()
                }, 2000)
            }
            feedbackText.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
        tts.shutdown()
        countDownTimer?.cancel()
    }
}
