package com.example.kfitness

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BlogPostAdapter(
    private val context: Context,
    private val blogPosts: List<BlogPost>,
    private val onItemClickListener: (BlogPost) -> Unit
) :
    RecyclerView.Adapter<BlogPostAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageUrl: ImageView = itemView.findViewById(R.id.imageUrl)
        val title: TextView = itemView.findViewById(R.id.title1)
        val description: TextView = itemView.findViewById(R.id.description)
        val author: TextView = itemView.findViewById(R.id.author)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val blogPost = blogPosts[position]

        Glide.with(context)
            .load(blogPost.imageUrl)
            .into(holder.imageUrl)

        holder.title.text = blogPost.title1
        holder.description.text = blogPost.description
        holder.author.text = blogPost.author

        // Set up item click listener
        holder.itemView.setOnClickListener {
            onItemClickListener(blogPost)
        }
    }

    override fun getItemCount(): Int {
        return blogPosts.size
    }
}

