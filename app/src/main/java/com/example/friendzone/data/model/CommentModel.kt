package com.example.friendzone.data.model


data class CommentModel(
    val userId: String = "",
    val text: String = "",
    val username: String = "", // New field for username
    val name: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

