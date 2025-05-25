package com.example.insightlearn

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class PaperHoldingOPositionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paper_holding_oposition)

        val videoView = findViewById<VideoView>(R.id.paperHoldingOPositionVideoView)
        val backButton = findViewById<Button>(R.id.backButton)

        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.paperholdingoposition}")
        videoView.setVideoURI(videoUri)
        videoView.start()

        backButton.setOnClickListener {
            finish()
        }
    }
}
