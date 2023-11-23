package com.example.kfitness

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.kfitness.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Check if the user is already signed in
        if (firebaseAuth.currentUser != null) {
            // User is already signed in, go to the main activity
            goToMainActivity()
        } else {
            // Set up Google Sign-In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

            binding.textView.setOnClickListener {
                startActivity(Intent(this, Homepage::class.java))
            }

            // Email Sign-In
            binding.btnSign.setOnClickListener {
                val email = binding.editTextTextEmailAddress.text.toString()
                val pass = binding.editTextTextPassword.text.toString()

                if (email.isNotEmpty() && pass.isNotEmpty()) {
                    signInUser(email, pass)
                } else {
                    Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            // Google Sign-In
            binding.GSign.setOnClickListener {
                signIn()
            }

            // Set up password visibility toggle
            val togglePasswordButton: ToggleButton = findViewById(R.id.togglePasswordButton)
            val passwordEditText: EditText = findViewById(R.id.editTextTextPassword)

            togglePasswordButton.setOnCheckedChangeListener { _, isChecked ->
                // Toggle password visibility based on the toggle button state
                passwordEditText.transformationMethod =
                    if (isChecked) HideReturnsTransformationMethod.getInstance()
                    else PasswordTransformationMethod.getInstance()
            }

            // Set fullscreen programmatically
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

            // Next Page TextView in MainActivity
            findViewById<TextView>(R.id.btnSignup).setOnClickListener {
                // Handle TextView click here
                startActivity(Intent(this, Register::class.java))
            }
        }
    }

    private fun signInUser(email: String, password: String) {
        if (password.length < 6) {
            Toast.makeText(this, "Password must have at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Custom message when sign-in is successful
                    Toast.makeText(this, "Sign-in successful! Welcome back!", Toast.LENGTH_SHORT).show()

                    // Pass the current user to the Push activity
                    val pushIntent = Intent(this@MainActivity, Push::class.java)
                    pushIntent.putExtra("currentUser", email) // Change this to your actual user identifier
                    startActivity(pushIntent)

                    goToMainActivity()
                } else {
                    Toast.makeText(
                        this,
                        "Wrong PASS FAILED!: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    private fun goToMainActivity() {
        startActivity(Intent(this, Homepage::class.java))
        finish() // Finish the current activity to prevent going back to it
    }

    private fun signIn() {
        // Sign out the current user from Firebase
        firebaseAuth.signOut()

        // Sign out of Google account
        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
            // Start Google Sign-In, allowing the user to choose an account
            startActivityForResult(mGoogleSignInClient.signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            try {
                val account =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                        .getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(
                    this,
                    "Google sign in failed: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Toast.makeText(
                        this,
                        "Signed in as ${user?.displayName}",
                        Toast.LENGTH_SHORT
                    ).show()
                    goToMainActivity()
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}