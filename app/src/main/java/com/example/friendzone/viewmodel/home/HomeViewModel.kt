package com.example.friendzone.viewmodel.home

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendzone.data.model.CommentModel
import com.example.friendzone.data.model.PostModel
import com.example.friendzone.data.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    private val postRef = db.getReference("posts")

    private val _postsAndUsers = MutableLiveData<List<Pair<PostModel, UserModel>>>()
    val postsAndUsers: LiveData<List<Pair<PostModel, UserModel>>> = _postsAndUsers

    private val _savedPostIds = MutableLiveData<List<String>>(emptyList())
    val savedPostIds: LiveData<List<String>> = _savedPostIds

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchPostsAndUsers()
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        currentUserId?.let {
            fetchSavedPosts(it) }

    }

    private fun fetchPostsAndUsers() {
        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModelScope.launch {
                    val result = mutableListOf<Pair<PostModel, UserModel>>()
                    val postChildren = snapshot.children.toList()
                    postChildren.forEach { postSnapshot ->
                        val post = postSnapshot.getValue(PostModel::class.java)
                        post?.let { it ->
                            fetchUserFromPost(it) { user ->
                                result.add(element = Pair(it, user))
                                if (result.size == postChildren.size) {
                                    result.sortByDescending { it.first.timeStamp.toLong() }
                                    _postsAndUsers.value = result
                                }
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun deletePost(postId: String) {
        val postRef = db.getReference("posts").child(postId)
        viewModelScope.launch {
            postRef.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Post deleted successfully, fetch the updated posts
                    fetchPostsAndUsers()
                    // Optionally, show a toast or notify the user about the success
                } else {
                    // Handle the error
                    task.exception?.let {
                        // Log or handle the error appropriately
                    }
                }
            }
        }
    }

    private fun fetchUserFromPost(post: PostModel, onResult: (UserModel) -> Unit) {
        db.getReference("users").child(post.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    viewModelScope.launch {
                        val user = snapshot.getValue(UserModel::class.java)
                        user?.let(onResult)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
    }

    fun toggleLike(postId: String, userId: String) {
        val postRef = postRef.child(postId)
        viewModelScope.launch {
            postRef.child("likes").child(userId).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    // Remove the like
                    postRef.child("likes").child(userId).removeValue().addOnCompleteListener {
                        fetchPostsAndUsers()
                    }
                } else {
                    // Add the like
                    postRef.child("likes").child(userId).setValue(true).addOnCompleteListener {
                        fetchPostsAndUsers()
                    }
                }
            }
        }
    }

    fun addComment(
        postId: String,
        userId: String,
        username: String,
        name: String,
        image: String,
        commentText: String,
        timeStamp: String
    ) {
        val postRef = db.getReference("posts").child(postId)
        postRef.child("comments").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val commentsList =
                    snapshot.children.mapNotNull { it.getValue(CommentModel::class.java) }
                        .toMutableList()
                viewModelScope.launch {
                    val newComment = CommentModel(
                        userId = userId, username = username,
                        name = name, text = commentText, image = image, timestamp = timeStamp
                    )
                    commentsList.add(newComment)
                    postRef.child("comments").setValue(commentsList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun fetchComments(postId: String, onResult: (List<CommentModel>) -> Unit) {
        val postRef = db.getReference("posts").child(postId).child("comments")
        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModelScope.launch {
                    val comments =
                        snapshot.children.mapNotNull { it.getValue(CommentModel::class.java) }
                    onResult(comments)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun fetchSavedPosts(currentUserId: String) {
        val userRef = db.getReference("users").child(currentUserId).child("savedPosts")
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val savedPosts = snapshot.children.mapNotNull { it.key }
                _savedPostIds.value = savedPosts
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun toggleSavePost(context: Context, postId: String, currentUserId: String) {
        val userRef = db.getReference("users").child(currentUserId).child("savedPosts")
        viewModelScope.launch {
            val isSaved = _savedPostIds.value?.contains(postId) == true
            if (isSaved) {
                userRef.child(postId).removeValue().addOnCompleteListener {
                    if (it.isSuccessful) {
                        _savedPostIds.value = _savedPostIds.value?.filter { it != postId }
                        Toast.makeText(
                            context,
                            "Post Has Been Removed from Saved",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                userRef.child(postId).setValue(true).addOnCompleteListener {
                    if (it.isSuccessful) {
                        _savedPostIds.value = _savedPostIds.value?.plus(postId) ?: listOf(postId)
                        Toast.makeText(
                            context,
                            "Post Has Been Saved",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    fun fetchSavedPost(userId: String): LiveData<List<PostModel>> {
        val savedPostLiveData = MutableLiveData<List<PostModel>>()
        db.getReference("users").child(userId).child("savedPosts")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    viewModelScope.launch {
                        val postIds = snapshot.children.mapNotNull { it.key }
                        fetchPostByIds(postIds) { posts ->
                            savedPostLiveData.value = posts
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        return savedPostLiveData
    }

     fun fetchPostByIds(postIds: List<String>, onResult: (List<PostModel>) -> Unit) {
        db.getReference("posts").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModelScope.launch {
                    val posts = postIds.mapNotNull { id ->
                        snapshot.child(id).getValue(PostModel::class.java)
                    }
                    onResult(posts)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun getUserById(userId: String): LiveData<UserModel> {
        val userLiveData = MutableLiveData<UserModel>()
        db.getReference("users").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    viewModelScope.launch {
                        val user = snapshot.getValue(UserModel::class.java)
                        userLiveData.value = user ?: UserModel()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        return userLiveData
    }
}
