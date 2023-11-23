package com.example.kfitness

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class Workout : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)


        // Set fullscreen programmatically
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        val imageView11 = findViewById<ImageView>(R.id.imageView11)

        imageView11.setOnClickListener {
            val intent = Intent(this, Push::class.java)
            startActivity(intent)
        }

        val imageView13 = findViewById<ImageView>(R.id.imageView13)

        imageView13.setOnClickListener {
            val intent = Intent(this, Pull::class.java)
            startActivity(intent)
        }

        val imageView14 = findViewById<ImageView>(R.id.imageView14)

        imageView14.setOnClickListener {
            val intent = Intent(this, Legs::class.java)
            startActivity(intent)
        }


        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.menu.findItem(R.id.Workout1)?.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Calculate -> {
                    val calculateIntent = Intent(this@Workout, Calculator::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.Home -> {
                    val calculateIntent = Intent(this@Workout, Homepage::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.Workout1 -> {
                    true
                }

                R.id.Setting -> {
                    val calculateIntent = Intent(this@Workout, Profile::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.Blog1 -> {
                    val calculateIntent = Intent(this@Workout, Blog::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                else -> false
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()



    }
}