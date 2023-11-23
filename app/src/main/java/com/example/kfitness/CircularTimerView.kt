package com.example.kfitness

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

class CircularTimerView(context: Context, attrs: AttributeSet) : View(context, attrs) {




    private var timerRunning = false
    private var timerSeconds = 0L
    private var timerListener: TimerListener? = null
    private var timer: CountDownTimer? = null

    private val paintCircle = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 20f
    }

    interface TimerListener {
        fun onStart()
        fun onStop()
    }

    fun setTimerListener(listener: TimerListener) {
        timerListener = listener
    }

    fun startTimer(seconds: Long) {
        timerSeconds = seconds
        timerRunning = true
        invalidate()

        timerListener?.onStart()

        // Start the countdown timer
        timer = object : CountDownTimer(seconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerSeconds = millisUntilFinished / 1000
                invalidate()
            }

            override fun onFinish() {
                timerRunning = false
                invalidate()
                timerListener?.onStop()
            }
        }.start()
    }

    fun stopTimer() {
        timer?.cancel()
        timerRunning = false
        invalidate()
        timerListener?.onStop()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(centerX, centerY) - paintCircle.strokeWidth / 2

        if (timerRunning) {
            val sweepAngle = (timerSeconds * 360f) / 60f
            canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, -90f, sweepAngle, false, paintCircle)
        } else {
            canvas.drawCircle(centerX, centerY, radius, paintCircle)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (timerRunning) {
                    stopTimer()
                } else {
                    startTimer(60L) // Change the timer duration as needed
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}
