package com.example.insightlearn

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SpeechTest4Activity : AppCompatActivity() {

    private lateinit var resultText: TextView
    private lateinit var statusText: TextView

    private val SPEECH_REQUEST_CODE = 1
    private val output1 = "Welcome to the class"   // Correct answer
    private var output2 = ""        // User's spoken answer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.speech4)

        resultText = findViewById(R.id.resultTextView)
        statusText = findViewById(R.id.statusTextView)
        val speakButton = findViewById<Button>(R.id.speakButton)
        val result = findViewById<Button>(R.id.resultButton)  // Reference to test2Button

        speakButton.setOnClickListener {
            startSpeechToText()
        }

        // Set OnClickListener for test2Button to go to SpeechTest2Activity
        result.setOnClickListener {
            val intent = Intent(this, lex_speech::class.java)  // Create an intent to navigate to SpeechTest2Activity
            startActivity(intent)  // Start the new activity
        }
    }

    private fun startSpeechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE)
        } catch (e: Exception) {
            resultText.text = "Speech recognition is not supported on this device."
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            output2 = result?.get(0)?.lowercase(Locale.ROOT) ?: ""

            resultText.text = "You said: $output2"

            if (output2 == output1) {
                statusText.text = "✅ Test Passed!"
                // Increment the global count if the answer is correct
                GlobalCounter.count += 1
            } else {
                statusText.text = "❌ Wrong answer, try again."
            }
        }
    }
}


