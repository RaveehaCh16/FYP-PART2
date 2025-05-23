package com.example.insightlearn

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class GDotDrawingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var drawPath = Path()
    private var drawPaint = Paint()
    private var canvasPaint = Paint(Paint.DITHER_FLAG)
    private var drawCanvas: Canvas? = null
    private var canvasBitmap: Bitmap? = null

    private var tracedPaths = mutableListOf<Path>()
    private var hintPath = Path()
    var tracingCompleted = false
    var stepCompletedListener: ((Boolean) -> Unit)? = null

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

    private fun createGHintPath(width: Float, height: Float) {
        val centerX = width / 2
        val centerY = height / 2
        val radius = Math.min(width, height) * 0.3f

        hintPath.reset()

        // Arc part (like C)
        val arcRect = RectF(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )
        hintPath.addArc(arcRect, 30f, 300f)

        // Horizontal line inside G (mouth)
        val lineStartX = centerX + radius * 0.3f
        val lineEndX = centerX + radius
        val lineY = centerY
        hintPath.moveTo(lineEndX, lineY)
        hintPath.lineTo(lineStartX, lineY)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap!!)
        createGHintPath(w.toFloat(), h.toFloat())
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

        canvas.drawPath(hintPath, hintPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> drawPath.moveTo(x, y)
            MotionEvent.ACTION_MOVE -> drawPath.lineTo(x, y)
            MotionEvent.ACTION_UP -> {
                val newPath = Path(drawPath)
                if (checkIfTracingCorrect(newPath)) {
                    tracedPaths.add(newPath)
                    tracingCompleted = true
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

    private fun checkIfTracingCorrect(drawnPath: Path): Boolean {
        val w = width.takeIf { it > 0 } ?: return false
        val h = height.takeIf { it > 0 } ?: return false

        val tempBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val tempCanvas = Canvas(tempBitmap)

        val pathPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 18f
            isAntiAlias = true
        }

        tempCanvas.drawPath(hintPath, pathPaint)
        val expectedPixels = IntArray(w * h)
        tempBitmap.getPixels(expectedPixels, 0, w, 0, 0, w, h)

        tempBitmap.eraseColor(Color.TRANSPARENT)
        tempCanvas.drawPath(drawnPath, pathPaint)
        val drawnPixels = IntArray(w * h)
        tempBitmap.getPixels(drawnPixels, 0, w, 0, 0, w, h)

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
        tracingCompleted = false
        invalidate()
    }

    fun isCompleted(): Boolean = tracingCompleted
}
