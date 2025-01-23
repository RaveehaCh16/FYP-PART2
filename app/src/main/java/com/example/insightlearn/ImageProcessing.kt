/*package com.example.insightlearn

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class ImagesActivity : AppCompatActivity() {

    private lateinit var interpreter: Interpreter
    private lateinit var imageView: ImageView
    private lateinit var resultText: TextView
    private var inputImageWidth = 224
    private var inputImageHeight = 224
    private var inputPixelSize = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.model)

        // Initialize views
        imageView = findViewById(R.id.imageView)
        resultText = findViewById(R.id.resultText)
        val predictButton: Button = findViewById(R.id.predictButton)

        // Load model
        val modelLoaded = loadModel()
        if (!modelLoaded) {
            resultText.text = "Failed to load model. Please restart the app."
            return
        }

        // Set up the predict button
        predictButton.setOnClickListener {
            lifecycleScope.launch {
                try {
                    predictImage()
                } catch (e: Exception) {
                    Log.e("ImagesActivity", "Prediction failed: ${e.localizedMessage}")
                    resultText.text = "An error occurred: ${e.localizedMessage}"
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::interpreter.isInitialized) {
            interpreter.close()
        }
    }

    /**
     * Load the TensorFlow Lite model from the assets folder.
     * @return True if the model was loaded successfully, false otherwise.
     */
    private fun loadModel(): Boolean {
        return try {
            val modelBuffer = loadModelFile("deepmodel.tflite")
            if (modelBuffer != null) {
                interpreter = Interpreter(modelBuffer)
                val inputShape = interpreter.getInputTensor(0).shape()
                inputImageWidth = inputShape[1]
                inputImageHeight = inputShape[2]
                Log.d("ImagesActivity", "Interpreter initialized successfully with input shape: $inputShape")
                true
            } else {
                Log.e("ImagesActivity", "Model buffer is null.")
                false
            }
        } catch (e: Exception) {
            Log.e("ImagesActivity", "Error initializing model: ${e.message}")
            e.printStackTrace()  // Print the stack trace for better debugging
            false
        }
    }


    /**
     * Perform prediction using the TensorFlow Lite model.
     */
    private suspend fun predictImage() {
        val bitmap = withContext(Dispatchers.IO) {
            loadImageFromInternalStorage(this@ImagesActivity, "MyDrawingFolder", "drawing.png")
        }

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap)
            val resizedBitmap = try {
                Bitmap.createScaledBitmap(bitmap, inputImageWidth, inputImageHeight, true)
            } catch (e: Exception) {
                Log.e("ImagesActivity", "Error resizing bitmap: ${e.message}")
                null
            }

            if (resizedBitmap != null) {
                val inputBuffer = bitmapToByteBuffer(resizedBitmap)
                val outputBuffer = Array(1) { FloatArray(1) }

                interpreter.run(inputBuffer, outputBuffer)

                val confidence = outputBuffer[0][0]
                resultText.text = if (confidence < 0.5) {
                    "The image is predicted as 'Dysgraphic'"
                } else {
                    "The image is predicted as 'Normal'"
                }
            } else {
                resultText.text = "Error processing the image. Please try again."
            }
        } else {
            resultText.text = "Failed to load image."
        }
    }

    /**
     * Load the TensorFlow Lite model file from the assets folder.
     */
    private fun loadModelFile(modelPath: String): ByteBuffer? {
        return try {
            val assetFileDescriptor = assets.openFd(modelPath)
            val fileInputStream = assetFileDescriptor.createInputStream()
            val fileChannel = fileInputStream.channel
            val modelBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, assetFileDescriptor.startOffset, assetFileDescriptor.declaredLength)
            Log.d("ImagesActivity", "Model file loaded successfully from assets.")
            modelBuffer
        } catch (e: IOException) {
            Log.e("ImagesActivity", "Error loading model from assets: ${e.message}")
            e.printStackTrace()  // Print stack trace for detailed debugging
            null
        }
    }



    /**
     * Load an image from internal storage.
     */
    private fun loadImageFromInternalStorage(context: Context, folderName: String, fileName: String): Bitmap? {
        return try {
            val folder = File(context.filesDir, folderName)
            val file = File(folder, fileName)
            if (file.exists()) {
                BitmapFactory.decodeStream(FileInputStream(file))
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ImagesActivity", "Error loading image: ${e.message}")
            null
        }
    }

    /**
     * Convert a bitmap into a ByteBuffer to feed into the TensorFlow Lite model.
     */
    private fun bitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val buffer = ByteBuffer.allocateDirect(4 * inputImageWidth * inputImageHeight * inputPixelSize)
        buffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(inputImageWidth * inputImageHeight)
        bitmap.getPixels(intValues, 0, inputImageWidth, 0, 0, inputImageWidth, inputImageHeight)

        for (pixelValue in intValues) {
            val r = ((pixelValue shr 16) and 0xFF) / 255.0f
            val g = ((pixelValue shr 8) and 0xFF) / 255.0f
            val b = (pixelValue and 0xFF) / 255.0f
            buffer.putFloat(r)
            buffer.putFloat(g)
            buffer.putFloat(b)
        }
        return buffer
    }
}
*/