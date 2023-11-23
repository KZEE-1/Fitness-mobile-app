package com.example.kfitness

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Blog : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog)

        // Set fullscreen programmatically
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("blog_posts")

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Set the selected item to "Blog1" when the activity is created
        bottomNavigationView.menu.findItem(R.id.Blog1)?.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Calculate -> {
                    // Handle click on the Calculate menu item
                    val calculateIntent = Intent(this@Blog, Calculator::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.Home -> {
                    val calculateIntent = Intent(this@Blog, Homepage::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.Workout1 -> {
                    val calculateIntent = Intent(this@Blog, Workout::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.Setting -> {
                    // Handle click on the Setting menu item
                    val calculateIntent = Intent(this@Blog, Profile::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.Blog1 -> {
                    // Already on the "Blog1" page, do nothing
                    true
                }

                else -> false
            }
        }

        val imageButton: ImageButton = findViewById(R.id.Addnewblog)

        imageButton.setOnClickListener {
            // Open the BlogDetails page here
            // You can start a new activity or fragment, depending on your app structure
            // For example, starting a new activity:
            val intent = Intent(this, Publish::class.java)
            startActivity(intent)
        }

        // Listen for changes in the data
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val blogPosts = mutableListOf<BlogPost>()

                for (postSnapshot in snapshot.children) {
                    val imageUrl = postSnapshot.child("imageUrl").getValue(String::class.java) ?: ""
                    val author = postSnapshot.child("author").getValue(String::class.java) ?: ""
                    val title = postSnapshot.child("title1").getValue(String::class.java) ?: ""
                    val description =
                        postSnapshot.child("description").getValue(String::class.java) ?: ""
                    val blogPostId = postSnapshot.key ?: ""

                    val blogPost = BlogPost(imageUrl, title, description, author, blogPostId)
                    blogPosts.add(blogPost)
                }

                blogPosts.reverse()

                // Set up the RecyclerView with the retrieved data and item click listener
                val adapter = BlogPostAdapter(this@Blog, blogPosts) { clickedBlogPost ->
                    // Handle item click, start Blogdetails activity with blogPostId
                    val intent = Intent(this@Blog, Blogdetails::class.java).apply {
                        putExtra("imageUrl", clickedBlogPost.imageUrl)
                        putExtra("author", clickedBlogPost.author)
                        putExtra("title", clickedBlogPost.title1)
                        putExtra("description", clickedBlogPost.description)
                        putExtra("blogPostId", clickedBlogPost.blogPostId)
                    }
                    startActivity(intent)
                }
                recyclerView.adapter = adapter


            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
