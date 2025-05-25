package com.example.insightlearn

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class OPositionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_o_position)

        val videoView = findViewById<VideoView>(R.id.oPositionVideo)
        val backButton = findViewById<Button>(R.id.backButton)

        // Load video from raw resource
        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.oposition}")
        videoView.setVideoURI(videoUri)
        videoView.start()

        // Back button closes the activity
        backButton.setOnClickListener {
            finish()
        }
    }
}
