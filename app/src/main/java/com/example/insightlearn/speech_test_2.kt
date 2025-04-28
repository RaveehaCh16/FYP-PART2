package com.example.insightlearn

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SpeechTest2Activity : AppCompatActivity() {

    private lateinit var resultText: TextView
    private lateinit var statusText: TextView
    private lateinit var topLabel: TextView // Reference to the TextView displaying the word

    private val SPEECH_REQUEST_CODE = 1
    private var output1 = ""  // Correct answer, will be set randomly
    private var output2 = ""  // User's spoken answer

    private val wordList = listOf("dad", "bib", "did", "bid", "pod", "pad", "tap", "dap") // List of words

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.speech2)

        resultText = findViewById(R.id.resultTextView)
        statusText = findViewById(R.id.statusTextView)
        topLabel = findViewById(R.id.topLabel)

        val speakButton = findViewById<Button>(R.id.speakButton)
        val test3Button = findViewById<Button>(R.id.test3)

        // Set a random word from the list as output1 and display it in the XML layout
        output1 = wordList.random() // Randomly select a word
        topLabel.text = "Speak word: $output1" // Update the TextView with the selected word

        speakButton.setOnClickListener {
            startSpeechToText()
        }

        test3Button.setOnClickListener {
            val intent = Intent(this, SpeechTest3Activity::class.java)
            startActivity(intent)
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
                GlobalCounter.count += 1
            } else {
                statusText.text = "❌ Wrong answer, try again."
            }
        }
    }
}
