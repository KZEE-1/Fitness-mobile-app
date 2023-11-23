package com.example.kfitness

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Generalsetting : AppCompatActivity() {

    private val databaseReference = FirebaseDatabase.getInstance().getReference("users")
    private lateinit var edtChangePassword: TextInputEditText
    private lateinit var togglePassword: CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generalsetting)

        // Set fullscreen programmatically
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        // Check if the user signed in with Google, if so, hide the change password UI
        checkGoogleSignIn()

        edtChangePassword = findViewById(R.id.edtChangePassword)
        togglePassword = findViewById(R.id.togglePassword)

        togglePassword.setOnCheckedChangeListener { _, isChecked ->
            togglePasswordVisibility(isChecked)
        }

        // Example for the Save button
        findViewById<Button>(R.id.btnSaved).setOnClickListener {
            saveDataToFirebase()
        }

        // Example for the Clear button
        findViewById<Button>(R.id.btnClear).setOnClickListener {
            clearData()
        }

        // Retrieve data from Firebase and populate the UI fields
        retrieveDataFromFirebase()
    }

    private fun saveDataToFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val newUsername = findViewById<EditText>(R.id.edtChangeUsername).text.toString()
        val newPassword = findViewById<EditText>(R.id.edtChangePassword).text.toString()
        val newAge = findViewById<EditText>(R.id.edtChangeAge).text.toString()
        val newWeight = findViewById<EditText>(R.id.edtChangeWeight).text.toString()
        val newHeight = findViewById<EditText>(R.id.edtChangeHeight).text.toString()

        // Check if the new password meets the minimum length requirement
        if (newPassword.isNotEmpty() && newPassword.length < 6) {
            Toast.makeText(this, "Password must be 6 characters or more", Toast.LENGTH_SHORT).show()
            return
        }

        // Retrieve the current user
        val user = FirebaseAuth.getInstance().currentUser

        // Retrieve the profile picture URL
        val profilePictureUrl = user?.photoUrl.toString()

        // Create a HashMap to store user data
        val userData = hashMapOf(
            "username" to newUsername,
            "age" to newAge,
            "weight" to newWeight,
            "height" to newHeight
        )

// Add password only if it's not empty
        if (newPassword.isNotEmpty()) {
            userData["password"] = newPassword
            // Retrieve the current user and include the profile picture URL if available
            FirebaseAuth.getInstance().currentUser?.let { user ->
                user.photoUrl?.let { profilePictureUrl ->
                    userData["profilePictureUrl"] = profilePictureUrl.toString()
                }
            }
        }



        // Check if the password has changed
        val isPasswordChanged = newPassword.isNotEmpty()

        // Use child() to specify the user ID and set the values
        databaseReference.child(userId).setValue(userData)
            .addOnSuccessListener {
                // Data added successfully

                // Change password only if a new password is provided
                if (isPasswordChanged) {
                    updatePassword(user, newPassword)
                }

                // Show a toast saying "Data saved successfully"
                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()

                // Finish the current activity and start it again
                finish()


                // Add extra to the intent to signal refresh in Profile activity
                val intent = Intent(this@Generalsetting, Homepage::class.java)
                intent.putExtra("refreshProfile", true)
                startActivity(intent)


            }
            .addOnFailureListener {
                // Handle failure
                Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
            }
    }


    private fun updatePassword(user: FirebaseUser?, newPassword: String) {
        user?.let {
            if (it.providerData.any { provider -> provider.providerId == GoogleAuthProvider.PROVIDER_ID }) {
                // User signed in with Google, don't show the "Password updated successfully" toast
                return
            }
        }

        user?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Password updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Log the exception to get more details about the error
                    task.exception?.let { exception ->
                        Log.e("PasswordUpdateError", "Failed to update password", exception)
                    }

                    // Display a more specific error message
                    val errorMessage = task.exception?.message ?: "Failed to update password"
                    Toast.makeText(
                        this,
                        errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    private fun clearData() {
        findViewById<EditText>(R.id.edtChangeUsername).text.clear()
        findViewById<EditText>(R.id.edtChangePassword).text.clear()
        findViewById<EditText>(R.id.edtChangeAge).text.clear()
        findViewById<EditText>(R.id.edtChangeWeight).text.clear()
        findViewById<EditText>(R.id.edtChangeHeight).text.clear()
    }

    private fun retrieveDataFromFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Read data from Firebase
        databaseReference.child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(User::class.java)

                    // Populate UI fields with the retrieved data
                    user?.let {
                        findViewById<EditText>(R.id.edtChangeUsername).setText(it.username)
                        findViewById<EditText>(R.id.edtChangePassword).setText(it.password)
                        findViewById<EditText>(R.id.edtChangeAge).setText(it.age)
                        findViewById<EditText>(R.id.edtChangeWeight).setText(it.weight)
                        findViewById<EditText>(R.id.edtChangeHeight).setText(it.height)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    Toast.makeText(
                        this@Generalsetting,
                        "Failed to retrieve data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        val text3: TextView = findViewById(R.id.text3)
        text3.setOnClickListener {
            // Handle click on Feedback
            // Start the FeedbackActivity or Fragment here
            val intent = Intent(this@Generalsetting, Feedback::class.java)
            startActivity(intent)
        }
    }

    private fun togglePasswordVisibility(showPassword: Boolean) {
        if (showPassword) {
            // Show password
            edtChangePassword.inputType = android.text.InputType.TYPE_CLASS_TEXT
        } else {
            // Hide password
            edtChangePassword.inputType =
                android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }

    private fun checkGoogleSignIn() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null && user.providerData.any { it.providerId == GoogleAuthProvider.PROVIDER_ID }) {
            // User signed in with Google, hide change password UI
            findViewById<Button>(R.id.btnSaved).isEnabled = true
            findViewById<EditText>(R.id.edtChangePassword).isEnabled = false
            findViewById<CheckBox>(R.id.togglePassword).isEnabled = false
        }
    }

}