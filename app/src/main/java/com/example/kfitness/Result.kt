package com.example.kfitness

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Result : AppCompatActivity() {

    private lateinit var resultTextView: TextView
    private lateinit var continueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        resultTextView = findViewById(R.id.textViewResult)
        continueButton = findViewById(R.id.buttonContinue)


        // Set fullscreen programmatically
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Get the result from the intent
        val result = intent.getIntExtra("CALCULATED_RESULT", 0)

        // Display the result with fade-in animation
        fadeIn(result.toString())

        // Handle button clicks
        continueButton.setOnClickListener {
            // Navigate to Decision activity and pass the result
            val intent = Intent(this, Decision::class.java)
            intent.putExtra("CALCULATED_RESULT", result)
            startActivity(intent)
            finish() // Optional: finish the Result activity if you don't want to go back to it
        }
    }

    private fun fadeIn(result: String) {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 1000

        val scaleUp = ScaleAnimation(
            0.5f,
            1f,
            0.5f,
            1f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        scaleUp.duration = 1000

        val animationSet = AnimationSet(true)
        animationSet.addAnimation(fadeIn)
        animationSet.addAnimation(scaleUp)

        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                // Animation ended, set the final text
                resultTextView.text = "Daily Calories: $result"
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        // Start the animation
        resultTextView.startAnimation(animationSet)


    }
}
