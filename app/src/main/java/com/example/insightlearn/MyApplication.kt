package com.example.insightlearn

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase here
        FirebaseApp.initializeApp(this)
    }
}