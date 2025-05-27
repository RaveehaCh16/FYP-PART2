package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DayHop_Game : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dayhop_game)

        val button: Button = findViewById(R.id.startGameButton)
        button.setOnClickListener {
            val intent = Intent(this, WhatComesNextActivity::class.java)
            startActivity(intent)
        }
    }
}

