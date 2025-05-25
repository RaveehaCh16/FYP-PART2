package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SpringStoryActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech

    private val storyText = """
        It’s spring! Aana puts on her yellow shirt.
        She sees pretty tulips and hears birds singing.
        She picks a daisy for her friend and spots a rabbit on the ground!
    """.trimIndent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.spring_display)

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
                val intent = Intent(this, SpringQuestionActivity::class.java)
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
