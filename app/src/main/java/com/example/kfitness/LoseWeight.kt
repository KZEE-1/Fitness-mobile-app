package com.example.kfitness

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class LoseWeight : AppCompatActivity() {

    // List to keep track of selected image resource IDs
    private val selectedImages = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lose_weight)


        // Set fullscreen programmatically
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

    }
}
