package com.example.insightlearn

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class JDotDrawingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var drawPath = Path()
    private var drawPaint = Paint()
    private var canvasPaint = Paint(Paint.DITHER_FLAG)
    private var drawCanvas: Canvas? = null
    private var _canvasBitmap: Bitmap? = null

    private var hintPath = Path()

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

    private fun buildJPath(width: Float, height: Float): Path {
        val path = Path()

        val topMargin = height * 0.15f
        val centerX = width / 2
        val topLineLength = width * 0.4f
        val topY = topMargin
        val verticalLineLength = height * 0.5f
        val bottomY = topY + verticalLineLength
        val hookRadius = width * 0.15f

        // 1. Top horizontal line
        val topLineStartX = centerX - topLineLength / 2
        val topLineEndX = centerX + topLineLength / 2
        path.moveTo(topLineStartX, topY)
        path.lineTo(topLineEndX, topY)

        // 2. Vertical line from center
        path.moveTo(centerX, topY)
        path.lineTo(centerX, bottomY)

        // 3. Hook like umbrella handle (arc curves to the left)
        val hookLeft = centerX - hookRadius * 2
        val arcRect = RectF(
            hookLeft,
            bottomY - hookRadius,
            centerX,
            bottomY + hookRadius
        )
        path.arcTo(arcRect, 0f, 180f, false)

        return path
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        _canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(_canvasBitmap!!)
        hintPath = buildJPath(w.toFloat(), h.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw saved user drawing
        _canvasBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, canvasPaint)
        }

        // Draw current drawing path
        canvas.drawPath(drawPath, drawPaint)

        // Draw hint path
        val hintPaint = Paint().apply {
            color = Color.GRAY
            strokeWidth = 6f
            style = Paint.Style.STROKE
            pathEffect = DashPathEffect(floatArrayOf(20f, 10f), 0f)
            isAntiAlias = true
        }
        canvas.drawPath(hintPath, hintPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                drawPath.moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                drawPath.lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                drawCanvas?.drawPath(drawPath, drawPaint)
                drawPath.reset()
            }
        }
        invalidate()
        return true
    }

    fun resetCanvas() {
        drawPath.reset()
        _canvasBitmap?.eraseColor(Color.TRANSPARENT)
        invalidate()
    }

    fun hasDrawingContent(): Boolean {
        _canvasBitmap?.let { bitmap ->
            val pixels = IntArray(bitmap.width * bitmap.height)
            bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
            return pixels.any { Color.alpha(it) != 0 }
        }
        return false
    }
}
