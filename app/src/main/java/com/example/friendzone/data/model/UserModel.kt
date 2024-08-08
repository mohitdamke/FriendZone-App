package com.example.friendzone.data.model

data class UserModel(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val bio: String = "",
    val userName: String = "",
    val imageUrl: String = "",
    val uid: String = "",
    val savedThreads: Map<String, Boolean> = emptyMap()  // Map of thread IDs to save status
)
