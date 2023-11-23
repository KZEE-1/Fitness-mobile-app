package com.example.kfitness

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Timer : AppCompatActivity() {

    private lateinit var tvTimer: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var btnReset: Button
    private var isRunning = false
    private var elapsedTime: Long = 0
    private val handler = Handler()
    private lateinit var btnWorkout: Button  // Add this line


    // BroadcastReceiver to receive timer updates from the service
    private val timerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == "TIMER_UPDATED") {
                    elapsedTime = it.getLongExtra("elapsedTime", 0)
                    updateTimerText(elapsedTime)
                }
            }
        }
    }

    private val runnable = object : Runnable {
        override fun run() {
            elapsedTime += 1000
            updateTimerText(elapsedTime)
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        // Set fullscreen programmatically
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        tvTimer = findViewById(R.id.tvTimer)
        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)
        btnReset = findViewById(R.id.btnReset)

        btnStart.setOnClickListener {
            startStopwatch()
        }

        btnStop.setOnClickListener {
            stopStopwatch()
        }

        btnReset.setOnClickListener {
            resetStopwatch()
        }

        // Register the BroadcastReceiver
        registerReceiver(timerReceiver, IntentFilter("TIMER_UPDATED"))

        // Restore the previous state if available
        savedInstanceState?.let {
            elapsedTime = it.getLong("elapsedTime", 0)
            updateTimerText(elapsedTime)
            isRunning = it.getBoolean("isRunning", false)

            if (isRunning) {
                startStopwatch()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // Save the current state before the activity is destroyed
        outState.putLong("elapsedTime", elapsedTime)
        outState.putBoolean("isRunning", isRunning)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the BroadcastReceiver
        unregisterReceiver(timerReceiver)
    }

    private fun startStopwatch() {
        if (!isRunning) {
            isRunning = true
            handler.postDelayed(runnable, 1000)
        }

        // Disable the start button and enable the stop button
        btnStart.isEnabled = false
        btnStop.isEnabled = true
    }

    private fun stopStopwatch() {
        if (isRunning) {
            isRunning = false
            handler.removeCallbacks(runnable)
        }

        // Enable the start button and disable the stop button
        btnStart.isEnabled = true
        btnStop.isEnabled = false

        // Enable the reset button
        btnReset.isEnabled = true
    }

    private fun resetStopwatch() {
        isRunning = false
        elapsedTime = 0
        updateTimerText(elapsedTime)

        // Enable the start button and disable the stop button
        btnStart.isEnabled = true
        btnStop.isEnabled = false

        // Disable the reset button
        btnReset.isEnabled = false
    }

    private fun updateTimerText(milliseconds: Long) {
        val seconds = (milliseconds / 1000).toInt()
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60

        val timeFormatted = String.format("%02d:%02d", minutes, remainingSeconds)
        tvTimer.text = "Timer: $timeFormatted"
    }
}