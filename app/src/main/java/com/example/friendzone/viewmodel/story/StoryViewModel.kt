package com.example.friendzone.viewmodel.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendzone.data.model.StoryModel
import com.example.friendzone.data.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class StoryViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    private val storyRef = db.getReference("story")
    private val userRef = db.getReference("users")

    private val _storyAndUsers = MutableLiveData<List<Pair<StoryModel, UserModel>>>()
    val storyAndUsers: LiveData<List<Pair<StoryModel, UserModel>>> = _storyAndUsers

    init {
        fetchStoryAndUsers()
    }

    private fun fetchStoryAndUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val storySnapshot = storyRef.get().await()
                val result = mutableListOf<Pair<StoryModel, UserModel>>()

                for (storySnap in storySnapshot.children) {
                    val story = storySnap.getValue(StoryModel::class.java)
                    if (story != null) {
                        val userSnapshot = userRef.child(story.userId).get().await()
                        val user = userSnapshot.getValue(UserModel::class.java)
                        if (user != null) {
                            result.add(story to user)
                        }
                    }
                }

                _storyAndUsers.postValue(result)

            } catch (e: Exception) {
                // Handle the exception, e.g., log it or notify the user
                e.printStackTrace()
            }
        }
    }


    private fun fetchUserFromStory(story: StoryModel, onResult: (UserModel) -> Unit) {
        db.getReference("users").child(story.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    user?.let(onResult)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

}

