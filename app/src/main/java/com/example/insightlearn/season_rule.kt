package com.example.insightlearn
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.insightlearn.R

class SeasonRulesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.season_gamerules) // Replace with your actual XML layout name

        val getStartedButton = findViewById<Button>(R.id.getStartedOverlay)

        getStartedButton.setOnClickListener {
            val intent = Intent(this, SummerStoryActivity::class.java) // Replace with your target activity
            startActivity(intent)
        }
    }
}
