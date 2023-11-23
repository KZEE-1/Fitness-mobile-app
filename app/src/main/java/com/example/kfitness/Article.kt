package com.example.kfitness

data class BlogPost(
    val imageUrl: String?,
    val author: String?,
    val title1: String,
    val description: String,
    val blogPostId: String = ""
)
