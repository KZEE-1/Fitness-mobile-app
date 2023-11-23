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

class Push : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private var isPlaying: Boolean = false
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var btnOpenSidebar: ImageButton
    private lateinit var btnOpenSidebar2: ImageButton
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push)


        // Set fullscreen programmatically
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        videoView = findViewById(R.id.videoView)
        videoView.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.benchpress))

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
                    val calculateIntent = Intent(this@Push, Calculator::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.Home -> {
                    val calculateIntent = Intent(this@Push, Homepage::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.Workout1 -> {
                    val calculateIntent = Intent(this@Push, Workout::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.Setting -> {
                    val calculateIntent = Intent(this@Push, Profile::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.Blog1 -> {
                    val calculateIntent = Intent(this@Push, Blog::class.java)
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
                R.id.menu_bench_press -> {
                    // Handle click for Bench Press
                    true
                }

                R.id.menu_overhead_press -> {
                    // Handle click for Overhead Press
                    startActivity(Intent(this@Push, Push2::class.java))
                    true
                }

                R.id.menu_lateral_raise -> {
                    // Handle click for Lateral Raise
                    startActivity(Intent(this@Push, Push3::class.java))
                    true
                }
                R.id.menu_dumbbel_fly -> {
                    // Handle click for Dumbbell Fly
                    startActivity(Intent(this@Push, Push4::class.java))
                    true
                }
                // Add more cases for other menu items as needed
                else -> false
            }
        }

    }
}


