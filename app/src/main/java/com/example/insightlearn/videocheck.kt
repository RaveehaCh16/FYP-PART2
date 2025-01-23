package com.example.insightlearn

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.VideoView
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
/*
class videocheckactivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check)

        val videoView: VideoView = findViewById(R.id.videoView)
        val playButton: Button = findViewById(R.id.playButton)

        // Sample video URL for testing
        val videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4"

        // Check if the URL is valid
        try {
            val videoUri = Uri.parse(videoUrl)
            videoView.setVideoURI(videoUri)

            // Set a listener for when the video is prepared
            videoView.setOnPreparedListener {
                Toast.makeText(this, "Video is ready to play", Toast.LENGTH_SHORT).show()
            }

            playButton.setOnClickListener {
                videoView.start()
            }
        } catch (e: Exception) {
            // Handle potential issues
            Toast.makeText(this, "Error: Unable to play video", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}
*/