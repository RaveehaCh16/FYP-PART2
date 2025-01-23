package com.example.insightlearn

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint: Paint = Paint()
    private val paths: MutableList<Path> = mutableListOf()
    private var currentPath: Path? = null

    init {
        paint.color = Color.BLACK
        paint.strokeWidth = 8f
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw all the paths on the canvas
        for (path in paths) {
            canvas.drawPath(path, paint)
        }
        currentPath?.let {
            canvas.drawPath(it, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPath = Path()
                currentPath?.moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                currentPath?.lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                currentPath?.let {
                    paths.add(it)
                }
                currentPath = null
            }
        }

        invalidate()  // Redraw the view
        return true
    }

    fun setStrokeWidth(width: Float) {
        paint.strokeWidth = width
        invalidate() // Redraw the view with the new stroke width
    }

    // Clear the drawing (optional, for reset functionality)
    fun clearDrawing() {
        paths.clear()
        invalidate()
    }

    // Save the drawing to a PNG file in the app's internal storage, inside a new folder
    fun saveDrawingToPNG(context: Context): Boolean {
        // Create a new directory within app's internal storage
        val directory = File(context.filesDir, "MyDrawingFolder")
        if (!directory.exists()) {
            directory.mkdir() // Create the folder if it doesn't exist
        }

        // Create a bitmap with the same size as the view
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Draw the current content of the view on the bitmap's canvas
        draw(canvas)

        // Define the path for saving the image
        var file = File(directory, "drawing.png")

        // Save the bitmap to the file
        try {
            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
            }
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }
}
