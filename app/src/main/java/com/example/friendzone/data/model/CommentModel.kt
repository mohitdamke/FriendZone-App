package com.example.friendzone.data.model


data class CommentModel(
    val userId: String = "",
    val text: String = "",
    val username: String = "", // New field for username
    val image: String = "", // New field for image URL
    val name: String = "",
    val timestamp: String = System.currentTimeMillis().toString()
)

