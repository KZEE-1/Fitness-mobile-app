package com.example.kfitness

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Decision : AppCompatActivity() {

    private lateinit var keepWeightTextView: TextView
    private lateinit var loseWeightTextView: TextView
    private lateinit var getBiggerTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decision)


        // Set fullscreen programmatically
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        keepWeightTextView = findViewById(R.id.KeepWeight)
        loseWeightTextView = findViewById(R.id.LoseWeight)
        getBiggerTextView = findViewById(R.id.GetBigger)

        // Get the result from the intent
        val result = intent.getIntExtra("CALCULATED_RESULT", 0)

        // Calculate 21% reduction for "Lose Weight"
        val loseWeightResult = (result * 0.79).toInt()

        // Calculate 21% increase for "Get Bigger"
        val getBiggerResult = (result * 1.21).toInt()

        // Display the results
        loseWeightTextView.text = "(Lose Weight): $loseWeightResult"
        keepWeightTextView.text = "(Keep Weight): $result"
        getBiggerTextView.text = "(Get Bigger): $getBiggerResult"
    }

    // Click handler for all ImageViews
    fun onImageClick(view: View) {
        when (view.id) {
            R.id.imageView6 -> navigateToRabbitActivity()
            R.id.imageView7 -> navigateToWolfActivity()
            R.id.imageView8 -> navigateToLionActivity()
        }
    }

    private fun navigateToRabbitActivity() {
        val intent = Intent(this, LoseWeight::class.java)
        startActivity(intent)
    }

    private fun navigateToWolfActivity() {
        val intent = Intent(this, MaintainWeight::class.java)
        startActivity(intent)
    }

    private fun navigateToLionActivity() {
        val intent = Intent(this, GainWeight::class.java)
        startActivity(intent)



    }
}
