package com.example.friendzone.data.model

data class PostModel(
    val post : String = "",
    val images: List<String>? = null, // Changed to List of URLs
    val userId : String = "",
    val storeKey : String = "",
    val timeStamp : String = "",
    val likes: Map<String, Boolean> = emptyMap(),  // User IDs and their like status
    val comments: List<CommentModel> = emptyList(),

    )