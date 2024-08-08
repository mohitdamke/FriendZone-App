package com.example.friendzone.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.net.Uri

object SharedPref {

    fun storeData(
        email: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: String,
        context: Context
    ) {

        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("name", name)
        editor.putString("bio", bio)
        editor.putString("userName", userName)
        editor.putString("imageUri", imageUri)
        editor.apply()
    }

    fun getUserName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("userName", "")!!
    }

    fun getEmail(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("email", "")!!
    }

    fun getName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("name", "")!!
    }

    fun getBio(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("bio", "")!!
    }

    fun getImageUrl(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("imageUri", "")!!
    }


}















