package com.example.kfitness

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class Publish : AppCompatActivity() {

    private lateinit var b_tittle: EditText
    private lateinit var b_author: EditText
    private lateinit var b_desc: EditText
    private lateinit var b_selectImage: View
    private lateinit var btnPublish: TextView

    private val storageReference = FirebaseStorage.getInstance().reference.child("blog_images")
    private val databaseReference = FirebaseDatabase.getInstance().getReference("blog_posts")

    private var selectedImageUri: Uri? = null

    companion object {
        private const val PICK_IMAGE_REQUEST_CODE = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish)



        // Set fullscreen programmatically
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        b_tittle = findViewById(R.id.b_tittle)
        b_author = findViewById(R.id.b_author)
        b_desc = findViewById(R.id.b_desc)
        b_selectImage = findViewById(R.id.b_selectImage)
        btnPublish = findViewById(R.id.btnPublish)

        b_selectImage.setOnClickListener {
            openImagePicker()
        }

        btnPublish.setOnClickListener {
            handlePublishButtonClick()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    private fun handlePublishButtonClick() {
        val title = b_tittle.text.toString()
        val author = b_author.text.toString()
        val description = b_desc.text.toString()

        if (title.isEmpty() || author.isEmpty() || description.isEmpty() || selectedImageUri == null) {
            Toast.makeText(
                this,
                "Please fill in all fields and select an image",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Update UI to indicate upload in progress
        btnPublish.isEnabled = false
        btnPublish.text = "UPLOADING..."

        uploadImage(selectedImageUri!!) { imageUrl ->
            val newBlogPost = BlogPost(imageUrl, author, title, description)
            uploadBlogPost(newBlogPost)
        }
    }





    private fun uploadImage(imageUri: Uri, onImageUpload: (String) -> Unit) {
        val imageRef =
            storageReference.child("${System.currentTimeMillis()}_${imageUri.lastPathSegment}")

        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully, get download URL
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    onImageUpload(uri.toString())
                }
            }
            .addOnFailureListener { e ->
                // Handle the failure case
                // You might want to update UI or show a toast message
                Toast.makeText(
                    this,
                    "Failed to upload image: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

                // Reset UI state
                btnPublish.isEnabled = true
                btnPublish.text = "PUBLISH"
            }
    }


    private fun uploadBlogPost(blogPost: BlogPost) {
        val postId = databaseReference.push().key
        if (postId != null) {
            databaseReference.child(postId).setValue(blogPost)
                .addOnSuccessListener {
                    // Update UI after successful upload
                    btnPublish.isEnabled = true
                    btnPublish.text = "PUBLISHED"
                    Toast.makeText(
                        this,
                        "Blog post published successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Add a new row for the published blog at the top
                    val blogLayout = findViewById<RelativeLayout>(R.id.blogLayout)
                    val newBlogRow = layoutInflater.inflate(R.layout.row, blogLayout, false)

                    // Set the data for the new row using blogPost
                    val titleTextView = newBlogRow.findViewById<TextView>(R.id.title1)
                    val descriptionTextView = newBlogRow.findViewById<TextView>(R.id.description)
                    val authorTextView = newBlogRow.findViewById<TextView>(R.id.author)


                    titleTextView.text = blogPost.title1
                    descriptionTextView.text = blogPost.description
                    authorTextView.text = blogPost.author

                    // Add the new row at the top
                    blogLayout.addView(newBlogRow, 0)

                    // Optionally, navigate to another activity after successful publish
                    startActivity(Intent(this, Blog::class.java))
                    finish()
                }
                .addOnFailureListener {
                    // Update UI on failure
                    btnPublish.isEnabled = true
                    btnPublish.text = "PUBLISH"
                    Toast.makeText(this, "Failed to publish blog post", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Handle the selected image URI
            selectedImageUri = data.data
            // Update your UI or perform any additional logic
        }
    }
}
