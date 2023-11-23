package com.example.kfitness

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView



class Homepage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)


        val shopeeButton: ImageButton = findViewById(R.id.imageButton2)

        shopeeButton.setOnClickListener {
            val shopeeLink = "https://shp.ee/zhkzf3h"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(shopeeLink))
            startActivity(intent)
        }

        val diceButton: ImageButton = findViewById(R.id.Dice)

        diceButton.setOnClickListener {
            // Handle the click, show a random workout-related toast
            showRandomToast()
        }


        val btnProfile: ImageView = findViewById(R.id.btnProfile)

        btnProfile.setOnClickListener {
            // Handle the click, navigate to the profile page
            val profileIntent = Intent(this@Homepage, Profile::class.java)
            profileIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(profileIntent)
            overridePendingTransition(0, 0) // Disable transition animation
        }


        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Set the selected item to "Home" when the activity is created
        bottomNavigationView.menu.findItem(R.id.Home)?.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Calculate -> {
                    // Handle click on the Calculate menu item
                    val calculateIntent = Intent(this@Homepage, Calculator::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0) // Disable transition animation
                    true
                }

                R.id.Home -> {
                    true
                }

                R.id.Workout1 -> {
                    val workoutIntent = Intent(this@Homepage, Workout::class.java)
                    workoutIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(workoutIntent)
                    overridePendingTransition(0, 0) // Disable transition animation
                    true
                }

                R.id.Setting -> {
                    // Handle click on the Setting menu item
                    val settingIntent = Intent(this@Homepage, Profile::class.java)
                    settingIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(settingIntent)
                    overridePendingTransition(0, 0) // Disable transition animation
                    true
                }

                R.id.Blog1 -> {
                    // Handle click on the Blog menu item
                    val blogIntent = Intent(this@Homepage, Blog::class.java)
                    blogIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(blogIntent)
                    overridePendingTransition(0, 0) // Disable transition animation
                    true
                }

                else -> false
            }
        }
        val timerButton: ImageButton = findViewById(R.id.Timer)

        timerButton.setOnClickListener {
            val intent = Intent(this, Timer::class.java)
            startActivity(intent)
        }


        val userMButton: ImageButton = findViewById(R.id.userM)
        userMButton.setOnClickListener {
            val intent = Intent(this, Faq::class.java)
            startActivity(intent)
        }


        val feedbackButton: ImageButton = findViewById(R.id.imageButton4)

        feedbackButton.setOnClickListener {
            val intent = Intent(this, Feedback::class.java)
            startActivity(intent)
        }

        val spotifyButton: ImageButton = findViewById(R.id.spotify)

        spotifyButton.setOnClickListener {
            Log.d("ButtonClicked", "Button clicked!") // Add this line for debugging

            val intent = packageManager.getLaunchIntentForPackage("com.spotify.music")

            if (intent != null) {
                startActivity(intent)
            } else {
                Log.e("SpotifyIntent", "Spotify app not installed!") // Add this line for debugging
                // Handle the case where Spotify is not installed
            }
        }

        fun showRandomToast() {
            val toastMessages = listOf(

                "Did you know? Exercise is a natural mood lifter!",
                "Get up and move! Your body will thank you.",
                "Exercise is a celebration of what your body can do.",
                "Sweat is just your fat crying.",
                "Fitness is not about being better than someone else; it's about being better than you used to be.",
                "Work hard, stay consistent, and make it happen.",
                "Your only limit is you.",
                "Stronger with every workout.",
                "Fitness is not a destination; it's a journey.",
                "No pain, no gain. Embrace the challenge!",
                "The only bad workout is the one that didn't happen.",
                "Wake up, work out, kick ass.",
                "Believe in yourself and all that you are.",
                "Fitness is the best medicine.",
                "Your health is an investment, not an expense.",
                "Stay committed to your fitness goals; results will follow.",
                "Fitness is the key to a happy and healthy life.",
                "Workout because you love your body, not because you hate it.",
                "Train like a beast, look like a beauty.",
                "Fitness: If it came in a bottle, everyone would have a great body."
            )


            val randomMessage = toastMessages.random()
            Toast.makeText(this, randomMessage, Toast.LENGTH_SHORT).show()
        }
        // Set fullscreen programmatically
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

    }
        fun showRandomToast() {
            val toastMessages = listOf(

                "Did you know? Exercise is a natural mood lifter!",
                "Get up and move! Your body will thank you.",
                "Exercise is a celebration of what your body can do.",
                "Sweat is just your fat crying.",
                "Fitness is not about being better than someone else; it's about being better than you used to be.",
                "Work hard, stay consistent, and make it happen.",
                "Your only limit is you.",
                "Stronger with every workout.",
                "Fitness is not a destination; it's a journey.",
                "No pain, no gain. Embrace the challenge!",
                "The only bad workout is the one that didn't happen.",
                "Wake up, work out, kick ass.",
                "Believe in yourself and all that you are.",
                "Fitness is the best medicine.",
                "Your health is an investment, not an expense.",
                "Stay committed to your fitness goals; results will follow.",
                "Fitness is the key to a happy and healthy life.",
                "Workout because you love your body, not because you hate it.",
                "Train like a beast, look like a beauty.",
                "Fitness: If it came in a bottle, everyone would have a great body."
            )



            val randomMessage = toastMessages.random()
            Toast.makeText(this, randomMessage, Toast.LENGTH_SHORT).show()
        }
    }