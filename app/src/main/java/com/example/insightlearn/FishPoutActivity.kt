package com.example.insightlearn

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class FishPoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fish_pout)

        val videoView = findViewById<VideoView>(R.id.fishPoutVideoView)
        val backButton = findViewById<Button>(R.id.backButton)

        // Load video from raw resource folder (replace liptrill_video with fishpout_video)
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.fishpout)
        videoView.setVideoURI(videoUri)
        videoView.start()

        backButton.setOnClickListener {
            finish()
        }
    }
}
