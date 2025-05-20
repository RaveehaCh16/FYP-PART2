package com.example.insightlearn

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.*
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import java.util.*
import android.graphics.Color
import android.view.View
import android.widget.Button



class SpeechBingoActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var bingoGrid: GridLayout
    private lateinit var tts: TextToSpeech
    private lateinit var currentWordDisplay: TextView
    private lateinit var promptButton: LottieAnimationView
    private var currentWord = ""
    private val SPEECH_CODE = 101
    private var gameOver = false
    lateinit var backbutton: Button


    private val wordList = mutableListOf(
        "apple", "mind", "cherry", "dog",
        "guitar", "house", "rice", "jelly",
        "lemon", "moon", "nurse", "ocean",
        "queen", "rain", "tree", "star",
    )

    private val cellViews = mutableListOf<TextView>()
    private val completedCells = mutableSetOf<Int>()
    private val spokenWords = mutableSetOf<String>()
    private val correctWords = mutableSetOf<String>()
    private val handler = Handler(Looper.getMainLooper())
    private var autoSpeakRunnable: Runnable? = null
    private var userResponded = false
    private var responseTimeoutRunnable: Runnable? = null
    private lateinit var bingoAnimation: LottieAnimationView
    enum class CellStatus { UNFILLED, CORRECT, INCORRECT, NO_RESPONSE }
    private val cellStatus = MutableList(16) { CellStatus.UNFILLED }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.speech_bingo)

        bingoGrid = findViewById(R.id.wordGrid)
        currentWordDisplay = findViewById(R.id.currentWordDisplay)
        promptButton = findViewById(R.id.promptButton)
        backbutton = findViewById(R.id.backButton)
        bingoAnimation = findViewById(R.id.bingoAnimation)
        promptButton.isEnabled = false

        tts = TextToSpeech(this, this)

        populateBingoGrid()

        promptButton.setOnClickListener {
            promptSpeechInput()
        }
        backbutton.setOnClickListener {
            val intent = Intent(this, DyslexiaTherapy_Portals::class.java)
            startActivity(intent)
        }
    }

    private fun populateBingoGrid() {
        val cellWidth = (410 / 4).toInt()
        val cellHeight = (400 / 4).toInt()

        wordList.forEachIndexed { index, word ->
            val cell = TextView(this).apply {
                text = word
                textSize = 24f
                setPadding(20, 20, 20, 20)
                setBackgroundResource(R.drawable.word_cell_background)
                layoutParams = GridLayout.LayoutParams().apply {
                    width = (cellWidth * 2.40).toInt()
                    height = cellHeight * 2
                    rightMargin = 10
                    topMargin = 10
                    gravity = android.view.Gravity.CENTER
                }
                gravity = android.view.Gravity.CENTER
            }
            cellViews.add(cell)
            bingoGrid.addView(cell)
        }
    }



    private fun speakRandomWord() {
        if (gameOver) return  // ✅ Early return if the game is already over

        if (wordList.isEmpty()) {
            tts.speak("Game over!", TextToSpeech.QUEUE_FLUSH, null, null)
            return
        }

        currentWord = wordList.random()
        wordList.remove(currentWord)
        spokenWords.add(currentWord)

        currentWordDisplay.text = "$currentWord"
        tts.speak(currentWord, TextToSpeech.QUEUE_FLUSH, null, null)

        promptButton.isEnabled = false
        userResponded = false

        // Cancel any existing timeout and set a new one
        responseTimeoutRunnable?.let { handler.removeCallbacks(it) }
        responseTimeoutRunnable = Runnable {
            if (!userResponded && !gameOver) {
                Toast.makeText(this, "No response. Moving to next word.", Toast.LENGTH_SHORT).show()
                markWordAsNoResponse(currentWord)
                if (!checkForBingo()) {
                    speakRandomWord()
                }
            }
        }
        handler.postDelayed(responseTimeoutRunnable!!, 10000)

        handler.postDelayed({
            if (!gameOver) promptButton.isEnabled = true
        }, 1000)
    }

    private fun markWordAsNoResponse(word: String) {
        cellViews.find { it.text.toString().contains(word, ignoreCase = true) }?.let { cell ->
            val cellIndex = cellViews.indexOf(cell)
            cellStatus[cellIndex] = CellStatus.NO_RESPONSE

            cell.setBackgroundColor(Color.parseColor("#FFFF00"))  // Yellow
            cell.text = "❓ "
        }
    }

    private fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        startActivityForResult(intent, SPEECH_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SPEECH_CODE && resultCode == Activity.RESULT_OK) {
            userResponded = true
            responseTimeoutRunnable?.let { handler.removeCallbacks(it) }

            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = results?.get(0)?.lowercase(Locale.ROOT) ?: ""

            val spokenWordsList = spokenText.split(" ")
            val wordMatched = spokenWordsList.all { it == currentWord.lowercase() }

            if (wordMatched) {
                markWordAsCorrect(currentWord)
                correctWords.add(currentWord)
                Toast.makeText(this, "✅ Yahoo! Correct Answer", Toast.LENGTH_SHORT).show()
            } else {
                markWordAsIncorrect(currentWord)
                Toast.makeText(this, "❌ Oops! Wrong Answer", Toast.LENGTH_SHORT).show()
            }

            if (checkForBingo()) return

            handler.postDelayed({ speakRandomWord() }, 1000)
        }
    }


    private fun markWordAsCorrect(word: String) {
        cellViews.find { it.text.toString().contains(word, ignoreCase = true) }?.let { cell ->
            val cellIndex = cellViews.indexOf(cell)
            completedCells.add(cellIndex)
            cellStatus[cellIndex] = CellStatus.CORRECT

            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(100)
            }

            cell.setBackgroundColor(resources.getColor(android.R.color.holo_green_light))
            cell.text = "✅ "
        }
    }

    private fun markWordAsIncorrect(word: String) {
        cellViews.find { it.text.toString().contains(word, ignoreCase = true) }?.let { cell ->
            val cellIndex = cellViews.indexOf(cell)
            cellStatus[cellIndex] = CellStatus.INCORRECT

            cell.setBackgroundColor(resources.getColor(android.R.color.holo_red_light))
            cell.text = "❌"  // Only cross
        }
    }

    private fun checkForBingo(): Boolean {
        val winningCombinations = listOf(
            listOf(0, 1, 2, 3),
            listOf(4, 5, 6, 7),
            listOf(8, 9, 10, 11),
            listOf(12, 13, 14, 15),

            listOf(0, 4, 8, 12),
            listOf(1, 5, 9, 13),
            listOf(2, 6, 10, 14),
            listOf(3, 7, 11, 15),

            listOf(0, 5, 10, 15),
            listOf(3, 6, 9, 12)
        )

        for (combo in winningCombinations) {
            val allFilled = combo.all { cellStatus[it] != CellStatus.UNFILLED }

            if (allFilled) {
                val allCorrect = combo.all { cellStatus[it] == CellStatus.CORRECT }

                gameOver = true
                if (allCorrect) {
                    tts.speak("Bingo! You win!", TextToSpeech.QUEUE_FLUSH, null, null)
                    Toast.makeText(this, "Bingo! You win!", Toast.LENGTH_SHORT).show()
                } else {
                    tts.speak("Bingo! I win!", TextToSpeech.QUEUE_FLUSH, null, null)
                    Toast.makeText(this, "Bingo! I win!", Toast.LENGTH_SHORT).show()
                }

                showBingoAnimation()
                return true
            }
        }

        return false
    }



    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
            speakRandomWord()
        }
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        autoSpeakRunnable?.let { handler.removeCallbacks(it) }
        responseTimeoutRunnable?.let { handler.removeCallbacks(it) }
        super.onDestroy()
    }

    private fun showBingoAnimation() {
        bingoAnimation.visibility = View.VISIBLE
        bingoAnimation.playAnimation()
    }

}
