package com.example.insightlearn

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.*
import java.util.concurrent.TimeUnit

class SpeechTest2Activity : AppCompatActivity() {

    private lateinit var resultText: TextView
    private lateinit var statusText: TextView
    private lateinit var topLabel: TextView
    private lateinit var konfettiView: KonfettiView
    private lateinit var learnButton: Button

    private val SPEECH_REQUEST_CODE = 1
    private var output1 = ""
    private var output2 = ""

    private val wordList = listOf("dad", "bib", "did", "bid", "pod", "pad", "tap", "dap")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.speech2)

        resultText = findViewById(R.id.resultTextView)
        statusText = findViewById(R.id.statusTextView)
        topLabel = findViewById(R.id.topLabel)
        konfettiView = findViewById(R.id.konfettiView)
        learnButton = findViewById(R.id.learnPronunciationButton)

        val speakButton = findViewById<Button>(R.id.speakButton)
        val nextButton = findViewById<Button>(R.id.nextButton)
        val backButton = findViewById<Button>(R.id.backButton)

        // Retain the same word unless it's a fresh start
        output1 = savedInstanceState?.getString("word")
            ?: intent.getStringExtra("word")
                    ?: wordList.random()

        topLabel.text = "Speak word: $output1"

        speakButton.setOnClickListener {
            startSpeechToText()
        }

        nextButton.setOnClickListener {
            val intent = Intent(this, SpeechTest3Activity::class.java)
            startActivity(intent)
        }

        learnButton.setOnClickListener {
            val intent = Intent(this, PronunciationActivity::class.java)
            intent.putExtra("word", output1)
            startActivityForResult(intent, 100)
        }

        backButton.setOnClickListener {
            val intent = Intent(this, SpeechTestActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("word", output1)
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

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            topLabel.text = "Speak word: $output1"
            return
        }

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            output2 = result?.get(0)?.lowercase(Locale.ROOT) ?: ""

            resultText.text = "You said: $output2"
            GlobalTotal.count += 1

            if (output2 == output1) {
                statusText.text = "✅ Test Passed!"
                GlobalCounter.count += 1
                learnButton.visibility = View.GONE
                celebrate()
            } else {
                statusText.text = "❌ Wrong answer, try again."
                learnButton.visibility = View.VISIBLE
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
