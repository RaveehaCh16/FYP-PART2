package com.example.insightlearn

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint()
    private val paths = mutableListOf<Path>()
    private var currentPath: Path? = null
    private var drawingBounds: Rect? = null

    init {
        paint.color = Color.BLACK
        paint.strokeWidth = 8f
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
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

        // Constrain touch within bounds (if set)
        drawingBounds?.let {
            if (!it.contains(x.toInt(), y.toInt())) return false
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPath = Path()
                currentPath?.moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                currentPath?.lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                currentPath?.let { paths.add(it) }
                currentPath = null
            }
        }

        invalidate()
        return true
    }

    fun setStrokeWidth(width: Float) {
        paint.strokeWidth = width
        invalidate()
    }

    fun clearDrawing() {
        paths.clear()
        invalidate()
    }

    fun saveDrawingToPNG(context: Context): Boolean {
        val directory = File(context.filesDir, "MyDrawingFolder")
        if (!directory.exists()) directory.mkdir()

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)

        val file = File(directory, "drawing.png")
        return try {
            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun setDrawingBounds(bounds: Rect) {
        drawingBounds = bounds
    }
}
