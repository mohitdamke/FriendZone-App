package com.example.friendzone.data.model

data class Message(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val chatId: String = ""
)

data class Chat(
    val messages: Map<String, Message> = mapOf()
)