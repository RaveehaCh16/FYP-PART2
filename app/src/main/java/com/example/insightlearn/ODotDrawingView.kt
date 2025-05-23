package com.example.insightlearn

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class ODotDrawingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var drawPath = Path()
    private var drawPaint = Paint()
    private var canvasPaint = Paint(Paint.DITHER_FLAG)
    private var drawCanvas: Canvas? = null
    private var canvasBitmap: Bitmap? = null

    private val tracedPaths = mutableListOf<Path>()
    private var completed = false
    var stepCompletedListener: ((Boolean) -> Unit)? = null

    private val referencePath = Path()

    init {
        setupDrawing()
    }

    private fun setupDrawing() {
        drawPaint.color = Color.BLUE
        drawPaint.isAntiAlias = true
        drawPaint.strokeWidth = 18f
        drawPaint.style = Paint.Style.STROKE
        drawPaint.strokeJoin = Paint.Join.ROUND
        drawPaint.strokeCap = Paint.Cap.ROUND
    }

    private fun setupReferencePath(width: Float, height: Float) {
        referencePath.reset()

        val padding = width / 6
        val left = padding
        val top = padding
        val right = width - padding
        val bottom = height - padding
        val rect = RectF(left, top, right, bottom)

        referencePath.addOval(rect, Path.Direction.CW)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap!!)
        setupReferencePath(w.toFloat(), h.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvasBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, canvasPaint)
        }

        for (path in tracedPaths) {
            canvas.drawPath(path, drawPaint)
        }

        canvas.drawPath(drawPath, drawPaint)

        val hintPaint = Paint().apply {
            color = Color.GRAY
            strokeWidth = 6f
            style = Paint.Style.STROKE
            pathEffect = DashPathEffect(floatArrayOf(20f, 10f), 0f)
        }

        canvas.drawPath(referencePath, hintPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> drawPath.moveTo(x, y)
            MotionEvent.ACTION_MOVE -> drawPath.lineTo(x, y)
            MotionEvent.ACTION_UP -> {
                val newPath = Path(drawPath)
                if (checkIfPathMatches(newPath)) {
                    tracedPaths.add(newPath)
                    completed = true
                    stepCompletedListener?.invoke(true)
                } else {
                    stepCompletedListener?.invoke(false)
                }
                drawPath.reset()
            }
        }
        invalidate()
        return true
    }

    private fun checkIfPathMatches(drawnPath: Path): Boolean {
        val width = width.takeIf { it > 0 } ?: return false
        val height = height.takeIf { it > 0 } ?: return false

        val tempBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val tempCanvas = Canvas(tempBitmap)

        val pathPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 18f
            isAntiAlias = true
        }

        // Draw reference path (O shape)
        tempCanvas.drawPath(referencePath, pathPaint)
        val expectedPixels = IntArray(width * height)
        tempBitmap.getPixels(expectedPixels, 0, width, 0, 0, width, height)

        // Clear and draw user's path
        tempBitmap.eraseColor(Color.TRANSPARENT)
        tempCanvas.drawPath(drawnPath, pathPaint)
        val drawnPixels = IntArray(width * height)
        tempBitmap.getPixels(drawnPixels, 0, width, 0, 0, width, height)

        var matchCount = 0
        for (i in expectedPixels.indices) {
            if (expectedPixels[i] == Color.BLACK && drawnPixels[i] == Color.BLACK) {
                matchCount++
            }
        }

        val totalExpected = expectedPixels.count { it == Color.BLACK }
        return totalExpected > 0 && (matchCount.toFloat() / totalExpected) > 0.3f
    }

    fun resetCanvas() {
        drawPath.reset()
        tracedPaths.clear()
        completed = false
        invalidate()
    }

    fun isCompleted(): Boolean = completed
}
