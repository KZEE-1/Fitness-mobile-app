package com.example.kfitness

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Blogdetails : AppCompatActivity() {

    private lateinit var imageUrl: ImageView
    private lateinit var author: TextView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var shareButton: FloatingActionButton
    private lateinit var deleteButton: FloatingActionButton
    private lateinit var backButton: ImageView
    private lateinit var blogPostId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_blogdetails)

        // Set fullscreen programmatically
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        imageUrl = findViewById(R.id.imageUrl)
        author = findViewById(R.id.author)
        title = findViewById(R.id.title1)
        description = findViewById(R.id.description)
        shareButton = findViewById(R.id.floatingActionButtonShare)
        deleteButton = findViewById(R.id.floatingActionButtonDelete)
        backButton = findViewById(R.id.Backbtn)

        val intent = intent
        blogPostId = intent.getStringExtra("blogPostId") ?: ""
        val receivedImageUrl = intent.getStringExtra("imageUrl")
        val receivedAuthor = intent.getStringExtra("author")
        val receivedTitle = intent.getStringExtra("title")
        val receivedDescription = intent.getStringExtra("description")

        // Log the received data for debugging
        Log.d("BlogDetails", "Received Image URL: $receivedImageUrl")
        Log.d("BlogDetails", "Received Author: $receivedAuthor")
        Log.d("BlogDetails", "Received Title: $receivedTitle")
        Log.d("BlogDetails", "Received Description: $receivedDescription")

        // Load image using Glide
        Glide.with(this).load(receivedImageUrl).into(imageUrl)

        // Check for null values before setting text
        author.text = receivedAuthor ?: "Author Not Available"
        title.text = receivedTitle ?: "Title Not Available"
        description.text = receivedDescription ?: "Description Not Available"

        // Set up click listeners
        backButton.setOnClickListener {
            finish() // Close the activity when the back button is clicked
        }

        shareButton.setOnClickListener {
            // Add your logic for sharing the blog post within the app
            val appLink = "kfitness://blogdetails?blogPostId=$blogPostId"
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Check out this blog post: $appLink"
                )
                type = "text/plain"
            }

            // Create a chooser dialog to let the user choose where to share the content
            val chooserIntent = Intent.createChooser(shareIntent, "Share this blog post via")

            // Verify that the intent will resolve to an activity before starting
            if (shareIntent.resolveActivity(packageManager) != null) {
                startActivity(chooserIntent)
            } else {
                // Handle the case where no activity can handle the share intent
                // You can show a message to the user or take other appropriate actions
                // For example, you can use a fallback mechanism like sharing a link or displaying an error message
            }
        }



        deleteButton.setOnClickListener {
            // Show confirmation dialog before deleting
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Blog Post")
            .setMessage("Are you sure you want to delete this blog post?")
            .setPositiveButton("Yes") { dialog, which ->
                // Delete the blog post
                deleteBlogPost()
                dialog.dismiss()
                finish() // Close the activity after deletion (you can customize this part)
            }
            .setNegativeButton("No") { dialog, which ->
                // Cancel the deletion
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteBlogPost() {
        // Get a reference to the blog post in the database using its unique identifier
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("blog_posts")
        databaseReference.child(blogPostId).removeValue()
    }
}
