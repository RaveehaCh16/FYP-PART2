package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class WinterStoryActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private val storyText = """
        It’s a cold winter day, and Aana is wearing her warm jacket and mittens.
        She built a snowman with a big smile and a carrot nose.
        Now, she’s playing with snowballs and having so much fun!
    """.trimIndent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.winter_display)

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
                val intent = Intent(this, WinterQuestionActivity::class.java)
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
