package com.example.kfitness

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class Welcome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set fullscreen programmatically
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_welcome)

        val signUpButton = findViewById<Button>(R.id.btnSignUpNext)
        val signInButton = findViewById<Button>(R.id.btnSignInNext)
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        signUpButton.setOnClickListener {
            val intent = Intent(this@Welcome, Register::class.java)
            startActivity(intent)
        }
        signInButton.setOnClickListener {
            val intent = Intent(this@Welcome, MainActivity::class.java)
            startActivity(intent)
        }

        if (currentUser != null) {
            // User is already authenticated, navigate to the next screen
            startActivity(Intent(this, Homepage::class.java))
            finish() // Optional: finish the WelcomeActivity so it's not in the back stack
        }
    }
}
