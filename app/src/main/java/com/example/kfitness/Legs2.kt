package com.example.kfitness

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class Legs2 : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private var isPlaying: Boolean = false
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var btnOpenSidebar: ImageButton
    private lateinit var btnOpenSidebar2: ImageButton
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_legs2)


        // Set fullscreen programmatically
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        videoView = findViewById(R.id.videoView)
        videoView.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.lunges))

        mediaPlayer = MediaPlayer.create(this, R.raw.lunges)

        val mediaController = MediaController(this)

        // Set the anchor view to the parent layout
        mediaController.setAnchorView(findViewById(R.id.videoView))

        // Customize the layout parameters to make the media controller smaller
        val params = mediaController.layoutParams as FrameLayout.LayoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT  // Set your desired width
        params.height = 120  // Set your desired height
        mediaController.layoutParams = params

        videoView.setMediaController(mediaController)

        videoView.setOnPreparedListener { mediaPlayer ->
            this.mediaPlayer = mediaPlayer
        }


        btnOpenSidebar = findViewById(R.id.btnOpenSidebar)
        btnOpenSidebar2 = findViewById(R.id.btnOpenSidebar2)
        navigationView = findViewById(R.id.navigationView)



        btnOpenSidebar.setOnClickListener {
            toggleNavigationView(true)
        }

        btnOpenSidebar2.setOnClickListener {
            toggleNavigationView(false)
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.menu.findItem(R.id.Workout1)?.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Calculate -> {
                    val calculateIntent = Intent(this@Legs2, Calculator::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.Home -> {
                    val calculateIntent = Intent(this@Legs2, Homepage::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.Workout1 -> {
                    val calculateIntent = Intent(this@Legs2, Workout::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.Setting -> {
                    val calculateIntent = Intent(this@Legs2, Profile::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.Blog1 -> {
                    val calculateIntent = Intent(this@Legs2, Blog::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                else -> false
            }
        }
    }

    private fun toggleNavigationView(show: Boolean) {
        if (show) {
            navigationView.visibility = View.VISIBLE
        } else {
            navigationView.visibility = View.GONE
        }

// Inside your existing setNavigationItemSelectedListener
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_squats -> {
                    // Handle click for Bench Press
                    startActivity(Intent(this@Legs2, Legs::class.java))
                    true
                }

                R.id.menu_lunges -> {
                    // Handle click for Overhead Press

                    true
                }
                R.id.menu_calf_raises -> {
                    // Handle click for Lateral Raise
                    startActivity(Intent(this@Legs2, Legs3::class.java))
                    true
                }

                // Add more cases for other menu items as needed
                else -> false
            }
        }

    }
}