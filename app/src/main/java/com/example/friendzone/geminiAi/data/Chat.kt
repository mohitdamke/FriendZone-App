package com.example.threadpractice.geminiAi.data

import android.graphics.Bitmap

data class Chat(
    val prompt : String,
    val image : Bitmap?,
    val isFromUser : Boolean
)