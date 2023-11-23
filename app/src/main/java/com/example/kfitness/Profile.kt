package com.example.kfitness

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class Profile : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var databaseReference: DatabaseReference
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var profilePictureImageView: ImageView
    private lateinit var storageReference: StorageReference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        // Set fullscreen programmatically
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )



        profilePictureImageView = findViewById(R.id.ProfilePicture)
        val changeImageButton: ImageButton = findViewById(R.id.btnChangeImage)

        // Initialize Firebase Storage reference
        storageReference = FirebaseStorage.getInstance().reference.child("profilePictures")

        changeImageButton.setOnClickListener {
            // Open the image picker
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }


        // Initialize Firebase
        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        userId = currentUser?.uid ?: ""
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)

        // Set up click listeners
        val imageView: ImageView = findViewById(R.id.btnBack)
        imageView.setOnClickListener {
            startActivity(Intent(this, Homepage::class.java))
        }

        val btnLogout: Button = findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            logoutUser()
        }



        val btnUpdate: Button = findViewById(R.id.buttonUpdate2)
        btnUpdate.setOnClickListener {
            // Navigate to GeneralSetting page
            val intent = Intent(this@Profile, Generalsetting::class.java)
            startActivity(intent)
        }

        // Call this function to display saved data when the activity is created
        displaySavedData()

        // Check for the extra to refresh the Profile activity
        if (intent.getBooleanExtra("refreshProfile", false)) {
            displaySavedData()
            // Finish the current activity to close it
            finish()

        }

        val btnRefresh: ImageButton = findViewById(R.id.btnRefresh)
        btnRefresh.setOnClickListener {
            // Call the function to refresh the profile
            refreshProfile()

            // Display a toast message indicating that the refresh is in progress
            showToast("Refresh")
        }

    }

    private fun logoutUser() {
        Log.d("Homepage", "logoutUser")
        FirebaseAuth.getInstance().signOut()

        // Redirect to the login activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Optional: Finish the current activity to prevent going back to it

        // Set fullscreen programmatically
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun displaySavedData() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    updateUIWithUserData(user)
                } else {
                    showToast("No data found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Failed to retrieve data: ${error.message}")
            }
        })
    }

    private fun updateUIWithUserData(user: User?) {
        if (user != null) {
            Log.d("Profile", "Updating UI with user data: $user")
            // Update your UI elements with the user data
            findViewById<TextView>(R.id.textUsername).text = "Username: ${user.username}"
            findViewById<TextView>(R.id.textAge).text = "Age: ${user.age}"
            findViewById<TextView>(R.id.textWeight).text = "Weight: ${user.weight} kg"
            findViewById<TextView>(R.id.textHeight).text = "Height: ${user.height} cm"

            // Check if the user has a profile picture URL
            if (!user.profilePictureUrl.isNullOrEmpty()) {
                // Set the profile picture CircleImageView with the URL using Glide
                Glide.with(this)
                    .load(user.profilePictureUrl)
                    .into(profilePictureImageView)
            } else {
                // If no profile picture, you can set a default image
                profilePictureImageView.setImageResource(R.drawable.profile_photo)
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get the selected image URI
            val imageUri: Uri? = data.data

            // Check if the URI is not null before uploading
            imageUri?.let {
                // Upload the selected image to Firebase Storage
                uploadImage(it)
            } ?: showToast("Failed to get image URI")
        }
    }

    private fun uploadImage(imageUri: Uri) {
        // Create a reference to the location where you want to store the image
        val imageRef = storageReference.child("user_profile_picture.jpg")

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                // Image upload success
                // Retrieve the download URL
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    // Update the user's profile picture URL in Firebase Realtime Database
                    databaseReference.child("profilePictureUrl").setValue(downloadUri.toString())

                    // Display a success toast message
                    showToast("Profile picture changed successfully!")

                    // Stop the rotation animation after the profile picture is uploaded
                    stopRotateRefreshButton()

                    // Finish the current activity and start it again
                    finish()

                    // Start the Profile activity with a new intent to refresh the page
                    val intent = Intent(this@Profile, Profile::class.java)
                    intent.putExtra("refreshProfile", true)
                    startActivity(intent)
                    // Finish the current activity to close it
                    finish()
                }
            }
            .addOnFailureListener {
                // Image upload failed
                // Display a failure toast message
                showToast("Failed to change profile picture")

                // Stop the rotation animation if image upload fails
                stopRotateRefreshButton()
            }
    }



    private fun refreshProfile() {
        // Rotate the refresh button while data is being refreshed
        rotateRefreshButton()

        // Inside the refreshProfile function
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)

                    // Update non-image data only
                    updateNonImageUIWithUserData(user)

                    // Stop the rotation animation after data is refreshed
                    stopRotateRefreshButton()
                } else {
                    showToast("No data found")

                    // Stop the rotation animation if no data is found
                    stopRotateRefreshButton()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Failed to retrieve data: ${error.message}")

                // Stop the rotation animation if data retrieval fails
                stopRotateRefreshButton()
            }
        })
    }


        private fun rotateRefreshButton() {
        val btnRefresh: ImageButton = findViewById(R.id.btnRefresh)
        val rotation = ObjectAnimator.ofFloat(btnRefresh, View.ROTATION, 0f, 360f)
        rotation.duration = 1000
        rotation.interpolator = AccelerateDecelerateInterpolator()
        rotation.repeatCount = ObjectAnimator.INFINITE
        rotation.start()
    }

    private fun stopRotateRefreshButton() {
        val btnRefresh: ImageButton = findViewById(R.id.btnRefresh)
        btnRefresh.rotation = 0f // Reset rotation to 0 after stopping the animation
        btnRefresh.clearAnimation()
    }

    private fun updateNonImageUIWithUserData(user: User?) {
        if (user != null) {
            Log.d("Profile", "Updating non-image UI with user data: $user")
            // Update your UI elements with the user data (excluding profile picture)
            findViewById<TextView>(R.id.textUsername).text = "Username: ${user.username}"
            findViewById<TextView>(R.id.textAge).text = "Age: ${user.age}"
            findViewById<TextView>(R.id.textWeight).text = "Weight: ${user.weight} kg"
            findViewById<TextView>(R.id.textHeight).text = "Height: ${user.height} cm"
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
