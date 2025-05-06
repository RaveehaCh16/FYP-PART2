package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class PronunciationActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var wordToPronounce: String
    private var speechRate: Float = 0.5f  // Initial speech rate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pronounce1)

        wordToPronounce = intent.getStringExtra("word") ?: "word"
        val wordTextView = findViewById<TextView>(R.id.wordTextView)
        val speakButton = findViewById<Button>(R.id.speakButton)
        val retryButton = findViewById<Button>(R.id.retryButton)
        val speedSeekBar = findViewById<SeekBar>(R.id.speedSeekBar)

        wordTextView.text = "How to pronounce: $wordToPronounce"

        tts = TextToSpeech(this, this)

        speakButton.setOnClickListener {
            tts.speak(wordToPronounce, TextToSpeech.QUEUE_FLUSH, null, "")
        }

        speedSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                speechRate = progress / 100f
                tts.setSpeechRate(speechRate)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        retryButton.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onBackPressed() {
        setResult(RESULT_OK)
        super.onBackPressed()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
            tts.setSpeechRate(speechRate)
        }
    }

    override fun onDestroy() {
        if (tts.isSpeaking) tts.stop()
        tts.shutdown()
        super.onDestroy()
    }
}
