package com.example.insightlearn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TherapyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.therapy_screen)

        val dyslexiaButton = findViewById<Button>(R.id.dyslexiaButton)
        val dysgraphiaButton = findViewById<Button>(R.id.dysgraphiaButton)
        val backButton = findViewById<Button>(R.id.backButton)
        val lipTrillButton = findViewById<Button>(R.id.lipTrillButton)
        val fishPoutButton = findViewById<Button>(R.id.fishPoutButton)
        val oPositionButton = findViewById<Button>(R.id.oPositionButton)
        val lipSmackingButton = findViewById<Button>(R.id.lipSmackingButton)
        val paperHoldingButton = findViewById<Button>(R.id.paperHoldingButton)
        val paperHoldingOPositionButton = findViewById<Button>(R.id.paperHoldingOPositionButton)
        val suckBlowButton = findViewById<Button>(R.id.suckBlowButton)
        val fishPoutSmackingButton = findViewById<Button>(R.id.fishPoutSmackingButton) // ✅ New Button

        dyslexiaButton.setOnClickListener {
            startActivity(Intent(this, DyslexiaTherapy_Portals::class.java))
        }

        dysgraphiaButton.setOnClickListener {
            startActivity(Intent(this, youtube_dysgraphia_therapy::class.java))
        }

        lipTrillButton.setOnClickListener {
            startActivity(Intent(this, LipTrillActivity::class.java))
        }

        fishPoutButton.setOnClickListener {
            startActivity(Intent(this, FishPoutActivity::class.java))
        }

        oPositionButton.setOnClickListener {
            startActivity(Intent(this, OPositionActivity::class.java))
        }

        lipSmackingButton.setOnClickListener {
            startActivity(Intent(this, LipSmackingActivity::class.java))
        }

        paperHoldingButton.setOnClickListener {
            startActivity(Intent(this, PaperHoldingActivity::class.java))
        }

        paperHoldingOPositionButton.setOnClickListener {
            startActivity(Intent(this, PaperHoldingOPositionActivity::class.java))
        }

        suckBlowButton.setOnClickListener {
            startActivity(Intent(this, SuckBlowActivity::class.java))
        }

        fishPoutSmackingButton.setOnClickListener {
            startActivity(Intent(this, FishPoutSmackingActivity::class.java)) // ✅ Launch Fish Pout Smacking Activity
        }

        backButton.setOnClickListener {
            startActivity(Intent(this, DetectionActivity::class.java))
        }
    }
}
