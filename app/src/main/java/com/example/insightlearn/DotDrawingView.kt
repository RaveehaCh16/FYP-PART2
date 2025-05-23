package com.example.insightlearn

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DotDrawingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var drawPath = Path()
    private var drawPaint = Paint()
    private var canvasPaint = Paint(Paint.DITHER_FLAG)
    private var drawCanvas: Canvas? = null
    private var canvasBitmap: Bitmap? = null

    private val tracedPaths = mutableListOf<Path>()
    private var _currentStep = 0
    val currentStep: Int get() = _currentStep
    private var totalSteps = 3
    var stepCompletedListener: ((Boolean) -> Unit)? = null

    private val steps = mutableListOf<Path>()
    private var stepPath = Path()

    var tracingLetter: Char = 'A' // Default is A, can be set to B externally

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

    private fun setupSteps(width: Float, height: Float) {
        steps.clear()

        when (tracingLetter.uppercaseChar()) {
            'A' -> setupStepsForA(width, height)
            'B' -> setupStepsForB(width, height)
            else -> setupStepsForA(width, height)
        }

        setupStepPath()
    }

    private fun setupStepsForA(width: Float, height: Float) {
        val centerX = width / 2
        val topY = height / 6
        val bottomY = height * 5 / 6
        val leftX = centerX - width / 5
        val rightX = centerX + width / 5
        val middleY = topY + (bottomY - topY) / 2

        val step1 = Path().apply {
            moveTo(leftX, bottomY)
            lineTo(centerX, topY)
        }

        val step2 = Path().apply {
            moveTo(centerX, topY)
            lineTo(rightX, bottomY)
        }

        val step3 = Path().apply {
            moveTo(leftX + 40f, middleY)
            lineTo(rightX - 40f, middleY)
        }

        steps.addAll(listOf(step1, step2, step3))
    }

    private fun setupStepsForB(width: Float, height: Float) {
        val startX = width / 3
        val startY = height / 6
        val endY = height * 5 / 6
        val middleY = (startY + endY) / 2
        val radiusX = width / 6
        val radiusY = (endY - startY) / 6

        val step1 = Path().apply {
            moveTo(startX, startY)
            lineTo(startX, endY)
        }

        val step2 = Path().apply {
            val rect = RectF(startX, startY, startX + 2 * radiusX, middleY)
            arcTo(rect, 270f, 180f)
        }

        val step3 = Path().apply {
            val rect = RectF(startX, middleY, startX + 2 * radiusX, endY)
            arcTo(rect, 270f, 180f)
        }

        steps.addAll(listOf(step1, step2, step3))
    }

    private fun setupStepPath() {
        stepPath.reset()
        if (_currentStep >= steps.size) return
        stepPath.addPath(steps[_currentStep])
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap!!)
        setupSteps(w.toFloat(), h.toFloat())
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

        canvas.drawPath(stepPath, hintPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> drawPath.moveTo(x, y)
            MotionEvent.ACTION_MOVE -> drawPath.lineTo(x, y)
            MotionEvent.ACTION_UP -> {
                val newPath = Path(drawPath)
                if (checkIfStepCorrect(newPath)) {
                    tracedPaths.add(newPath)
                    _currentStep++
                    if (_currentStep < steps.size) {
                        setupStepPath()
                        stepCompletedListener?.invoke(false)
                    } else {
                        stepCompletedListener?.invoke(true)
                    }
                } else {
                    stepCompletedListener?.invoke(false)
                }
                drawPath.reset()
            }
        }
        invalidate()
        return true
    }

    private fun checkIfStepCorrect(drawnPath: Path): Boolean {
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

        tempCanvas.drawPath(stepPath, pathPaint)

        val expectedPixels = IntArray(width * height)
        tempBitmap.getPixels(expectedPixels, 0, width, 0, 0, width, height)

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
        _currentStep = 0
        setupStepPath()
        invalidate()
    }

    fun isCompleted(): Boolean = _currentStep >= steps.size
}
