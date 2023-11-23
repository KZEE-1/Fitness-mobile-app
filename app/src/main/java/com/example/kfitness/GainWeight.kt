package com.example.kfitness

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.kfitness.databinding.ActivityGainWeightBinding

class GainWeight : AppCompatActivity() {

    private lateinit var binding: ActivityGainWeightBinding // Replace with your actual binding class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGainWeightBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Set fullscreen programmatically
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )



        // Add similar logic for other sections if needed
    }
}
