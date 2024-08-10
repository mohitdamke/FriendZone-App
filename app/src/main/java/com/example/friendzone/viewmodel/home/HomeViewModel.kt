package com.example.friendzone.viewmodel.home

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.friendzone.data.model.CommentModel
import com.example.friendzone.data.model.PostModel
import com.example.friendzone.data.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeViewModel(): ViewModel() {


    private val db = FirebaseDatabase.getInstance()
    val post = db.getReference("posts")

    private var _postsAndUsers = MutableLiveData<List<Pair<PostModel, UserModel>>>()
    val postsAndUsers: LiveData<List<Pair<PostModel, UserModel>>> = _postsAndUsers

    private var _savedPost = MutableLiveData<List<PostModel>>()
    val savedPost: LiveData<List<PostModel>> = _savedPost

    private val _savedPostIds = MutableLiveData<List<String>>()
    val savedPostIds: LiveData<List<String>> = _savedPostIds


    init {
        fetchPostsAndUsers {
            _postsAndUsers.value = it
        }
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        fetchSavedPost(currentUserId)

    }

    private fun fetchPostsAndUsers(onResult: (List<Pair<PostModel, UserModel>>) -> Unit) {

        post.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val result = mutableListOf<Pair<PostModel, UserModel>>()

                for (postSnapshot in snapshot.children) {

                    val post = postSnapshot.getValue(PostModel::class.java)
                    post.let {
                        fetchUserFromPost(it!!) { user ->
                            result.add(0, it to user)

                            if (result.size == snapshot.childrenCount.toInt()) {
                                onResult(result)
                            }

                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun fetchUserFromPost(post: PostModel, onResult: (UserModel) -> Unit) {
        db.getReference("users").child(post.userId)
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

    fun toggleLike(postId: String, userId: String) {
        val postRef = FirebaseDatabase.getInstance().getReference("posts").child(postId)
        postRef.child("likes").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // User has already liked, remove the like
                        postRef.child("likes").child(userId).removeValue()
                    } else {
                        // User has not liked, add the like
                        postRef.child("likes").child(userId).setValue(true)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
    }


    fun addComment(
        postId: String,
        userId: String,
        username: String,
        name: String,
        commentText: String
    ) {
        val postRef = db.getReference("posts").child(postId)
        postRef.child("comments").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val commentsList = mutableListOf<CommentModel>()
                snapshot.children.mapNotNullTo(commentsList) { it.getValue(CommentModel::class.java) }

                val newComment = CommentModel(
                    userId = userId, username = username,
                    name = name, text = commentText
                )
                commentsList.add(newComment)

                postRef.child("comments").setValue(commentsList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }


    fun fetchComments(postId: String, onResult: (List<CommentModel>) -> Unit) {
        val postRef =
            FirebaseDatabase.getInstance().getReference("posts").child(postId).child("comments")
        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val comments =
                    snapshot.children.mapNotNull { it.getValue(CommentModel::class.java) }
                onResult(comments)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun toggleSavePost(postId: String, context: Context) {
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef = db.getReference("users").child(currentUserId).child("savedPost")

        userRef.child(postId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Post is already saved, unsave it
                    userRef.child(postId).removeValue()
                    // Remove postId from the saved post IDs list
                    _savedPostIds.value = _savedPostIds.value?.filter { it != postId }
                    Toast.makeText(
                        context                        ,
                        "Post Has Been Removed from Saved",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Post is not saved, save it
                    userRef.child(postId).setValue(true)
                    // Add postId to the saved post IDs list
                    _savedPostIds.value = _savedPostIds.value?.plus(postId) ?: listOf(postId)
                    Toast.makeText(context, "Post Has Been Saved", Toast.LENGTH_SHORT).show()
                    
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun fetchSavedPost(userId: String): LiveData<List<PostModel>> {
        val savedPostLiveData = MutableLiveData<List<PostModel>>()

        db.getReference("users").child(userId).child("savedPost")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val postIds = snapshot.children.mapNotNull { it.key }
                    fetchPostByIds(postIds) { posts ->
                        savedPostLiveData.value = posts
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })

        return savedPostLiveData
    }

    private fun fetchPostByIds(postIds: List<String>, onResult: (List<PostModel>) -> Unit) {
        db.getReference("posts").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val posts = postIds.mapNotNull { id ->
                    snapshot.child(id).getValue(PostModel::class.java)
                }
                onResult(posts)
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
                    val user = snapshot.getValue(UserModel::class.java)
                    userLiveData.value = user?: UserModel()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        return userLiveData
    }




}

