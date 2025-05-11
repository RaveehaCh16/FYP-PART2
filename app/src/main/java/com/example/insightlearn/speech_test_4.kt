package com.example.insightlearn

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.*
import java.util.concurrent.TimeUnit

class SpeechTest4Activity : AppCompatActivity() {

    private lateinit var resultText: TextView
    private lateinit var statusText: TextView
    private lateinit var konfettiView: KonfettiView
    private lateinit var topLabel: TextView
    private lateinit var learnButton: Button

    private val SPEECH_REQUEST_CODE = 1
    private var output1 = ""
    private var output2 = ""

    // Replaced bestCorrectCount with latestCorrectCount
    private var latestCorrectCount = 0
    private var countedAlready = false

    private val phrases = listOf(
        "there was a cat that eat a rat and after that sat on a yellow mat",
        "the cat found a ball it kick the ball and ran fast",
        "a puppy saw a frog at the pond the frog jumps",
        "john had a small goldfish it swims round and round all day"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.speech4)

        resultText = findViewById(R.id.resultTextView)
        statusText = findViewById(R.id.statusTextView)
        konfettiView = findViewById(R.id.konfettiView)
        topLabel = findViewById(R.id.topLabel)
        learnButton = findViewById(R.id.learnPronunciationButton)

        val speakButton = findViewById<Button>(R.id.speakButton)
        val backButton = findViewById<Button>(R.id.backButton)
        val nextButton = findViewById<Button>(R.id.nextButton)
        val homeButton = findViewById<Button>(R.id.homebutton)
        val settingsButton = findViewById<Button>(R.id.settingsbutton)

        output1 = savedInstanceState?.getString("output1") ?: intent.getStringExtra("phrase")
                ?: phrases.random()

        topLabel.text = "Speak this sentence:\n\n$output1"

        speakButton.setOnClickListener { startSpeechToText() }

        learnButton.setOnClickListener {
            val intent = Intent(this, PronunciationActivity::class.java)
            intent.putExtra("word", output1)
            startActivityForResult(intent, 100)
        }

        backButton.setOnClickListener {
            val intent = Intent(this, SpeechTest3Activity::class.java)
            startActivity(intent)
            finish()
        }

        nextButton.setOnClickListener {
            // Add the latestCorrectCount to GlobalCounter when 'Next' is pressed
            GlobalCounter.count += latestCorrectCount
            if (!countedAlready) {
                GlobalTotal.count += output1.split(" ").size
                countedAlready = true
            }
            val intent = Intent(this, lex_speech_result::class.java)
            startActivity(intent)
            finish()
        }

        homeButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("output1", output1)
    }

    private fun startSpeechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE)
        } catch (e: Exception) {
            Toast.makeText(this, "Speech recognition not supported.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            topLabel.text = "Speak this sentence:\n\n$output1"
            return
        }

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            output2 = result?.get(0)?.lowercase(Locale.ROOT) ?: ""

            if (output2.isBlank()) {
                statusText.text = "❌ No speech detected. Please try again."
                resultText.text = ""
                return
            }

            val words1 = output1.lowercase(Locale.ROOT).split(" ")
            val words2 = output2.lowercase(Locale.ROOT).split(" ")

            if (words2.size < words1.size / 2) {
                statusText.text = "❌ Too few words detected. Please speak the full sentence."
                resultText.text = ""
                learnButton.visibility = View.VISIBLE
                return
            }

            val coloredResult = SpannableStringBuilder()
            var correctCount = 0
            var addedMissingOnce = false

            for (i in words1.indices) {
                val word = if (i < words2.size) words2[i] else ""
                val expected = words1[i]
                val start = coloredResult.length

                when {
                    word == expected -> {
                        correctCount++
                        coloredResult.append(word)
                        coloredResult.setSpan(
                            ForegroundColorSpan(android.graphics.Color.BLACK),
                            start, coloredResult.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    word.isNotBlank() -> {
                        coloredResult.append(word)
                        coloredResult.setSpan(
                            ForegroundColorSpan(android.graphics.Color.RED),
                            start, coloredResult.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    !addedMissingOnce -> {
                        coloredResult.append("?")
                        coloredResult.setSpan(
                            ForegroundColorSpan(android.graphics.Color.RED),
                            start, coloredResult.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        addedMissingOnce = true
                    }
                }

                if (i != words1.size - 1) coloredResult.append(" ")
            }

            resultText.text = coloredResult
            // Update latestCorrectCount with the current attempt's correct count
            latestCorrectCount = correctCount

            if (correctCount == words1.size) {
                statusText.text = "✅ Test Passed!"
                learnButton.visibility = View.GONE
                celebrate()
            } else {
                statusText.text = "❌ Some words were incorrect. Try again."
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
