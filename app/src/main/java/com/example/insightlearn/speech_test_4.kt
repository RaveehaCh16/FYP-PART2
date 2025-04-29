package com.example.insightlearn

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.*
import java.util.concurrent.TimeUnit

class SpeechTest4Activity : AppCompatActivity() {

    private lateinit var resultText: TextView
    private lateinit var statusText: TextView

    private val SPEECH_REQUEST_CODE = 1
    private var output1 = ""   // Correct answer (random phrase)
    private var output2 = ""   // User's spoken answer
    private lateinit var konfettiView: KonfettiView

    // List of phrases to be displayed randomly
    private val phrases = listOf(
        "there was a cat that ate a rat, and after that sat on a yellow mat.",
        "The cat found a ball,it kicked the ball and ran fast",
        "A puppy saw a frog at the pond,the frog jumped",
        "Mia had a small goldfish,it swam round and round all day",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.speech4)

        resultText = findViewById(R.id.resultTextView)
        statusText = findViewById(R.id.statusTextView)
        val speakButton = findViewById<Button>(R.id.speakButton)
        val test4Button = findViewById<Button>(R.id.resultButton)
        val topLabel = findViewById<TextView>(R.id.topLabel)  // Add this line to reference topLabel
        konfettiView = findViewById(R.id.konfettiView)
        // Set a random phrase from the list when the activity starts
        output1 = phrases.random() // Randomly choose a phrase from the list
        topLabel.text = "Speak this sentence: \n\n$output1"    // ✅ Update topLabel, not resultText

        speakButton.setOnClickListener {
            startSpeechToText()
        }

        test4Button.setOnClickListener {
            val intent = Intent(this, lex_speech_result::class.java)
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

            if (output2 == output1.lowercase(Locale.ROOT)) {
                statusText.text = "✅ Test Passed!"
                // Increment the global count if the answer is correct
                GlobalCounter.count += 1
                celebrate()
            } else {
                statusText.text = "❌ Wrong answer, try again."
            }
        }
    }
    private fun celebrate() {
        val emitterConfig = Emitter(duration = 2, TimeUnit.SECONDS).perSecond(100)
        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfff44336.toInt(), 0xff4caf50.toInt(), 0xff2196f3.toInt()),
            emitter = emitterConfig,
            position = nl.dionsegijn.konfetti.core.Position.Relative(0.5, 0.3)
        )
        konfettiView.start(party)
    }
}
