package com.example.insightlearn

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class OPositionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_o_position)

        val videoView: VideoView = findViewById(R.id.oPositionVideo)
        val backButton = findViewById<Button>(R.id.backButton)

        val videoPath = "android.resource://" + packageName + "/" + R.raw.oposition
        videoView.setVideoURI(Uri.parse(videoPath))

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        videoView.start()
    }
}
