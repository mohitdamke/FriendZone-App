package com.example.threadpractice.geminiAi.data

import android.graphics.Bitmap
import com.example.threadpractice.geminiAi.api.CommonApi
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ChatData {


    suspend fun getResponse(prompt: String): Chat {
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = CommonApi().api_key
        )
        try {
            val response = withContext(Dispatchers.IO) {
                generativeModel.generateContent(prompt)
            }

            return Chat(
                prompt = response.text ?: "",
                image = null,
                isFromUser = false,
            )

        } catch (e: Exception) {
            return Chat(
                prompt = e.message ?: "",
                image = null,
                isFromUser = false,
            )
        }

    }

    suspend fun getResponseWithImage(prompt: String, bitmap: Bitmap): Chat {
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro-vision",
            apiKey = CommonApi().api_key
        )
        try {

            val inputContent = content {
                image(bitmap)
                text(prompt)
            }


            val response = withContext(Dispatchers.IO) {
                generativeModel.generateContent(prompt)
            }

            return Chat(
                prompt = response.text ?: "",
                image = null,
                isFromUser = false,
            )

        } catch (e: Exception) {
            return Chat(
                prompt = e.message ?: "",
                image = null,
                isFromUser = false,
            )
        }

    }
}