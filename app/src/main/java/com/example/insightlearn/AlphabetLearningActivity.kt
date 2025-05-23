package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AlphabetLearningActivity : AppCompatActivity() {

    private val alphabetData = listOf(
        Pair("A", R.drawable.letter_a), Pair("B", R.drawable.letter_b),
        Pair("C", R.drawable.letter_c), Pair("D", R.drawable.letter_d),
        Pair("E", R.drawable.letter_e), Pair("F", R.drawable.letter_f),
        Pair("G", R.drawable.letter_g), Pair("H", R.drawable.letter_h),
        Pair("I", R.drawable.letter_i), Pair("J", R.drawable.letter_j),
        Pair("K", R.drawable.letter_k), Pair("L", R.drawable.letter_l),
        Pair("M", R.drawable.letter_m), Pair("N", R.drawable.letter_n),
        Pair("O", R.drawable.letter_o), Pair("P", R.drawable.letter_p),
        Pair("Q", R.drawable.letter_q), Pair("R", R.drawable.letter_r),
        Pair("S", R.drawable.letter_s), Pair("T", R.drawable.letter_t),
        Pair("U", R.drawable.letter_u), Pair("V", R.drawable.letter_v),
        Pair("W", R.drawable.letter_w), Pair("X", R.drawable.letter_x),
        Pair("Y", R.drawable.letter_y), Pair("Z", R.drawable.letter_z)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alphabet_learning)

        // Back button functionality
        val backButton = findViewById<TextView>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val gridLayout = findViewById<GridLayout>(R.id.alphabetGrid)

        for ((letter, imageRes) in alphabetData) {
            val itemLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(16, 16, 16, 16)
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                }
            }

            val imageView = ImageView(this).apply {
                setImageResource(imageRes)
                layoutParams = LinearLayout.LayoutParams(150, 150)
            }

            val textView = TextView(this).apply {
                text = letter
                textSize = 24f
                textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            }

            itemLayout.setOnClickListener {
                val intent = when (letter) {
                    "A" -> Intent(this, LetterATracingActivity::class.java)
                    "B" -> Intent(this, LetterBTracingActivity::class.java)
                    "C" -> Intent(this, LetterCTracingActivity::class.java)
                    "D" -> Intent(this, LetterDTracingActivity::class.java)
                    "E" -> Intent(this, LetterETracingActivity::class.java)
                    "F" -> Intent(this, LetterFTracingActivity::class.java)
                    "G" -> Intent(this, LetterGTracingActivity::class.java)
                    "H" -> Intent(this, LetterHTracingActivity::class.java)
                    "I" -> Intent(this, LetterITracingActivity::class.java)
                    "J" -> Intent(this, LetterJTracingActivity::class.java)
                    "K" -> Intent(this, LetterKTracingActivity::class.java)
                    "L" -> Intent(this, LetterLTracingActivity::class.java)
                    "M" -> Intent(this, LetterMTracingActivity::class.java)
                    "N" -> Intent(this, LetterNTracingActivity::class.java)
                    "O" -> Intent(this, LetterOTracingActivity::class.java)
                    "P" -> Intent(this, LetterPTracingActivity::class.java)
                    "Q" -> Intent(this, LetterQTracingActivity::class.java)
                    "R" -> Intent(this, LetterRTracingActivity::class.java)
                    "S" -> Intent(this, LetterSTracingActivity::class.java)
                    "T" -> Intent(this, LetterTTracingActivity::class.java)
                    "U" -> Intent(this, LetterUTracingActivity::class.java)
                    "V" -> Intent(this, LetterVTracingActivity::class.java)
                    "W" -> Intent(this, LetterWTracingActivity::class.java)
                    "X" -> Intent(this, LetterXTracingActivity::class.java)
                    "Y" -> Intent(this, LetterYTracingActivity::class.java)
                    "Z" -> Intent(this, LetterZTracingActivity::class.java)
                    else -> null
                }
                intent?.let { startActivity(it) }
            }

            itemLayout.addView(imageView)
            itemLayout.addView(textView)
            gridLayout.addView(itemLayout)
        }
    }
}
