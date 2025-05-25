package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SummerStoryActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech

    private val storyText = """
        Aana is at the beach, wearing her cool sunglasses and eating yummy ice cream.
        She builds a big sandcastle.
        A little crab walks by, and Aana watches it with a big smile!
    """.trimIndent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.summer_display)

        tts = TextToSpeech(this, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
            tts.setSpeechRate(0.5f)  // Slow speech

            tts.speak(storyText, TextToSpeech.QUEUE_FLUSH, null, "springStory")

            val normalDuration = estimateSpeechDuration(storyText)
            val adjustedDuration = (normalDuration * 2) // Because speed is 0.5x, duration doubles

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, SummerQuestionActivity::class.java)
                startActivity(intent)
                finish()
            }, adjustedDuration + 250L)  // wait 0.5s after speech ends
        }
    }



    private fun estimateSpeechDuration(text: String): Long {
        val wordsPerMinute = 150
        val words = text.split("\\s+".toRegex()).size
        return (words * 60_000L) / wordsPerMinute
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
