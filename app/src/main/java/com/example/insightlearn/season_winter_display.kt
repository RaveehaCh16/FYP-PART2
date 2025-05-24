package com.example.insightlearn
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.insightlearn.R
import com.yourpackage.yourapp.SpringQuestionActivity
import com.yourpackage.yourapp.SummerQuestionActivity
import com.yourpackage.yourapp.WinterQuestionActivity

class WinterStoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.winter_display) // Replace with your actual XML filename

        // Delay of 5 seconds (5000 milliseconds)
        Handler(Looper.getMainLooper()).postDelayed({
            // Navigate to next activity
            val intent = Intent(this, WinterQuestionActivity::class.java) // Replace with your target activity
            startActivity(intent)
            finish() // Optional: prevents coming back to this screen with back button
        }, 5000)
    }
}
