package com.example.insightlearn

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class LipSmackingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lip_smacking)

        val videoView = findViewById<VideoView>(R.id.lipSmackingVideoView)
        val backButton = findViewById<Button>(R.id.backButton)

        // Load video from raw resource
        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.lipsmack}")
        videoView.setVideoURI(videoUri)
        videoView.start()

        backButton.setOnClickListener {
            finish()
        }
    }
}
