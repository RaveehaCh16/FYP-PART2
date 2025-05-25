package com.example.insightlearn

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class LipTrillActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lip_trill)

        val videoView = findViewById<VideoView>(R.id.lipTrillVideoView)
        val backButton = findViewById<Button>(R.id.backButton)

        // Load a video from raw resource or URI
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.liptrill)
        videoView.setVideoURI(videoUri)
        videoView.start()

        backButton.setOnClickListener {
            finish()
        }
    }
}
