package com.example.friendzone.viewmodel.story

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.friendzone.data.model.StoryModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class AddStoryViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    private val storyRef = db.getReference("story")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted

    fun saveImage(
        userId: String, imageUri: Uri
    ) {

        val storyKey = storyRef.push().key ?: return
        val imageRef = Firebase.storage.reference.child("story/$storyKey.jpg")

        val uploadTask = imageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                saveStory(userId = userId, storyKey = storyKey, imageUrl = uri.toString())
            }.addOnFailureListener { exception ->
                Log.d("SaveImage", "Failed to get image URL: ${exception.message}")
            }
        }.addOnFailureListener { exception ->
            Log.d("SaveImage", "Failed to upload image: ${exception.message}")
        }
    }

    fun saveStory(imageUrl: String, storyKey: String, userId: String) {
        // Generate a new key for the story
        val newStoryRef = storyRef.push()
        val storyKey = newStoryRef.key ?: return

        // Create a StoryModel object with the generated key
        val storyData = StoryModel(
            imageStory = imageUrl,
            userId = userId,
            timeStamp = System.currentTimeMillis(),
            storyKey = storyKey
        )

        // Save the story data under the generated key
        newStoryRef.setValue(storyData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _isPosted.value = true
            } else {
                _isPosted.value = false
                Log.d("SaveStory", "Failed to save story: ${task.exception?.message}")
            }
        }
    }


}










