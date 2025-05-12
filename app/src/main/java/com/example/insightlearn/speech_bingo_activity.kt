package com.example.insightlearn

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SpeechBingoActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var bingoGrid: GridLayout
    private lateinit var promptButton: Button
    private lateinit var tts: TextToSpeech
    private var currentWord = ""
    private val SPEECH_CODE = 101
    private val wordList = listOf("apple", "dog", "sun", "ball", "car", "book", "cat", "hat", "tree")
    private val cellViews = mutableListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.speech_bingo)

        bingoGrid = findViewById(R.id.bingoGrid)
        promptButton = findViewById(R.id.promptButton)
        tts = TextToSpeech(this, this)

        populateBingoGrid()

        promptButton.setOnClickListener {
            speakRandomWord()
        }
    }

    private fun populateBingoGrid() {
        wordList.shuffled().take(9).forEach { word ->
            val cell = TextView(this).apply {
                text = word
                textSize = 24f
                setPadding(16, 16, 16, 16)
                setBackgroundResource(android.R.drawable.btn_default)
                setOnClickListener {
                    Toast.makeText(this@SpeechBingoActivity, "Say: $word", Toast.LENGTH_SHORT).show()
                }
            }
            cellViews.add(cell)
            bingoGrid.addView(cell)
        }
    }

    private fun speakRandomWord() {
        currentWord = wordList.random()
        tts.speak("Say the word: $currentWord", TextToSpeech.QUEUE_FLUSH, null, null)

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        startActivityForResult(intent, SPEECH_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SPEECH_CODE && resultCode == Activity.RESULT_OK) {
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = results?.get(0)?.lowercase(Locale.ROOT) ?: ""

            if (spokenText == currentWord.lowercase()) {
                markWordAsCorrect(currentWord)
                Toast.makeText(this, "✅ Correct!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "❌ Try again!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun markWordAsCorrect(word: String) {
        cellViews.find { it.text.toString().contains(word, ignoreCase = true) }?.let { cell ->
            // Haptic feedback
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(100)
            }

            // Animation: scale up
            cell.animate().scaleX(1.3f).scaleY(1.3f).setDuration(200).withEndAction {
                // Rotate
                cell.animate().rotationBy(360f).setDuration(500).withEndAction {
                    // Scale back
                    cell.animate().scaleX(1f).scaleY(1f).setDuration(200).start()

                    // Mark visually
                    cell.setBackgroundColor(resources.getColor(android.R.color.holo_orange_light))
                    cell.text = "✅ ${cell.text}"
                }.start()
            }.start()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
        }
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }
}
