package com.example.friendzone.viewmodel.post

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.friendzone.data.model.PostModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PostViewModel() : ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    private val postRef = db.getReference("posts")


    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted

    fun saveImages(
        post: String,
        userId: String,
        imageUris: List<Uri>
    ) {
        val storyKey = postRef.push().key ?: return
        val storageRef = Firebase.storage.reference
        val uploadTasks = imageUris.mapIndexed { index, uri ->
            val imageRef = storageRef.child("posts/${storyKey}_$index.jpg")
            imageRef.putFile(uri).continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                imageRef.downloadUrl
            }
        }

        // Wait for all uploads to complete
        Tasks.whenAllComplete(uploadTasks).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val imageUri = uploadTasks.map { it.result.toString() }
                saveData(
                    post = post,
                    userId = userId,
                    storeKey = storyKey,
                    images = imageUri
                )
            } else {
                _isPosted.value = false
                task.exception?.let { ex ->
                    Log.e("PostViewModel", "Image upload failed", ex)
                }
            }
        }
    }

    fun saveData(
        post: String,
        userId: String,
        images: List<String>?,
        storeKey: String
    ) {
        val newPostRef = postRef.child(storeKey)
        val postData = PostModel(
            post = post,
            images = images ?: emptyList(),
            userId = userId,
            storeKey = storeKey,
            timeStamp = System.currentTimeMillis().toString(),
            likes = emptyMap(),
            comments = emptyList()
        )

        newPostRef.setValue(postData).addOnSuccessListener {
            _isPosted.value = true
        }.addOnFailureListener {
            _isPosted.value = false
            Log.e("PostViewModel", "Data save failed")
        }
    }
}
