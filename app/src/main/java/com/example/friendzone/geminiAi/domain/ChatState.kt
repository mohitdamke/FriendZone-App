package com.example.friendzone.geminiAi.domain

import android.graphics.Bitmap
import com.example.friendzone.geminiAi.data.Chat


data class ChatState(
    val chatList: MutableList<Chat> = mutableListOf(),
    val prompt : String = "",
    val bitmap: Bitmap? = null
)