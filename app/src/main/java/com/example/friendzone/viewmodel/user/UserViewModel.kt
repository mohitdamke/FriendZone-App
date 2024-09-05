package com.example.friendzone.viewmodel.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendzone.data.model.ChatModel
import com.example.friendzone.data.model.PostModel
import com.example.friendzone.data.model.StoryModel
import com.example.friendzone.data.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class UserViewModel : ViewModel() {

    var postsFetched = false
    var storyFetched = false
    private val db = FirebaseDatabase.getInstance()
    private val postRef = db.getReference("posts")
    val storyRef = db.getReference("story")
    private val userRef = db.getReference("users")

    private val _chatMessages = MutableLiveData<List<ChatModel>>()
    val chatMessages: LiveData<List<ChatModel>> get() = _chatMessages

    val chatRef = db.getReference("chats")

    private val _posts = MutableLiveData(listOf<PostModel>())
    val posts: LiveData<List<PostModel>> get() = _posts

    private val _story = MutableLiveData(listOf<StoryModel>())
    val story: LiveData<List<StoryModel>> get() = _story

    private val _followerList = MutableLiveData(listOf<String>())
    val followerList: LiveData<List<String>> get() = _followerList

    private val _followingList = MutableLiveData(listOf<String>())
    val followingList: LiveData<List<String>> get() = _followingList

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> = _deleteSuccess

    private val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
    private val _currentUser = MutableLiveData<UserModel?>()
    val currentUser: LiveData<UserModel?> get() = _currentUser


    private var job: Job? = null

    init {
        startStoryCleanupTask()
        fetchUserDetails(currentUserUid)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()  // Cancel the job when the ViewModel is cleared
    }

    private fun startStoryCleanupTask() {
        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                deleteExpiredStories()
                delay(TimeUnit.HOURS.toMillis(1))  // Check every hour
            }
        }
    }

    private fun deleteExpiredStories() {
        val currentTime = System.currentTimeMillis()
        val cutoffTime = currentTime - TimeUnit.HOURS.toMillis(24)

        storyRef.orderByChild("timeStamp").endAt(cutoffTime.toDouble())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { storySnapshot ->
                        val story = storySnapshot.getValue(StoryModel::class.java)
                        val storyKey = storySnapshot.key
                        viewModelScope.launch {

                            if (storyKey != null && story != null) {
                                // Remove the story from the database
                                storyRef.child(storyKey).removeValue()
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            story.imageStory?.let { imageUrl ->
                                                if (imageUrl.isNotEmpty()) {
                                                    Firebase.storage.getReferenceFromUrl(imageUrl)
                                                        .delete()
                                                        .addOnSuccessListener {
                                                            Log.d(
                                                                "DeleteStory",
                                                                "Image deleted successfully from storage"
                                                            )
                                                        }
                                                        .addOnFailureListener { exception ->
                                                            Log.d(
                                                                "DeleteStory",
                                                                "Failed to delete image from storage: ${exception.message}"
                                                            )
                                                        }
                                                }
                                            }
                                            Log.d(
                                                "DeleteStory",
                                                "Story deleted successfully from database"
                                            )
                                        } else {
                                            Log.d(
                                                "DeleteStory",
                                                "Failed to delete story from database: ${task.exception?.message}"
                                            )
                                        }
                                    }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("DeleteStory", "Failed to delete story: ${error.message}")
                }
            })
    }

    private val _users = MutableLiveData<UserModel?>()
    val users: LiveData<UserModel?> get() = _users

    fun fetchUsers(uid: String) {
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModelScope.launch {

                    val user = snapshot.getValue(UserModel::class.java)
                    if (user != null && user.uid == uid) {
                        _users.value = user
                    } else {
                        Log.e("UserViewModel", "User data is null for UID: $uid")
                        _users.value = UserModel() // or some default UserModel instance
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("UserViewModel", "Error fetching user data: ${error.message}")
                _users.value = UserModel() // or some default UserModel instance
            }
        })
    }

    fun fetchUserPosts(currentUserId: String) {
        FirebaseFirestore.getInstance()
            .collection("posts")
            .whereEqualTo("userId", currentUserId)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) {
                    // Handle error
                    return@addSnapshotListener
                }

                val posts = snapshot.documents.mapNotNull { it.toObject(PostModel::class.java) }
                _posts.value = posts
            }
    }
    fun fetchPosts(uid: String) {
        if (postsFetched) return
        postRef.orderByChild("userId").equalTo(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    viewModelScope.launch {

                        val postList = snapshot.children.mapNotNull { data ->
                            data.getValue(PostModel::class.java)
                        }
                        _posts.postValue(postList)
                        postsFetched = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun fetchStory(uid: String) {
        if (storyFetched) return
        storyRef.orderByChild("userId").equalTo(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    viewModelScope.launch {

                        val storyList = snapshot.children.mapNotNull {
                            val story = it.getValue(StoryModel::class.java)
                            story?.apply {
                                storyKey = it.key ?: "" // Set the key for each story
                            }
                        }
                        _story.postValue(storyList)
                        storyFetched = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("FetchStory", "Failed to fetch stories: ${error.message}")
                }
            })
    }

    fun fetchUserDetails(uid: String) {
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                _currentUser.postValue(user)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("UserViewModel", "Error fetching user data: ${error.message}")
                _currentUser.postValue(null)
            }
        })
    }

    fun deleteStory(storyKey: String) {
        storyRef.child(storyKey).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val story = snapshot.getValue(StoryModel::class.java)
                viewModelScope.launch {

                    if (story != null) {
                        storyRef.child(storyKey).removeValue().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                story.imageStory?.let { imageUrl ->
                                    if (imageUrl.isNotEmpty()) {
                                        Firebase.storage.getReferenceFromUrl(imageUrl)
                                            .delete()
                                            .addOnSuccessListener {
                                                Log.d(
                                                    "DeleteStory",
                                                    "Image deleted successfully from storage"
                                                )
                                            }
                                            .addOnFailureListener { exception ->
                                                Log.d(
                                                    "DeleteStory",
                                                    "Failed to delete image from storage: ${exception.message}"
                                                )
                                            }
                                    }
                                }
                                Log.d("DeleteStory", "Story deleted successfully from database")
                                val currentStoryList = _story.value?.toMutableList()
                                currentStoryList?.remove(story)
                                _story.postValue(currentStoryList)
                                _deleteSuccess.postValue(true)
                            } else {
                                Log.d(
                                    "DeleteStory",
                                    "Failed to delete story from database: ${task.exception?.message}"
                                )
                                _deleteSuccess.postValue(false)
                            }
                        }
                    } else {
                        Log.d("DeleteStory", "Story with key $storyKey not found")
                        _deleteSuccess.postValue(false)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DeleteStory", "Failed to delete story: ${error.message}")
            }
        })
    }

    private val firestoreDb = Firebase.firestore
    fun followOrUnfollowUser(userId: String, currentUserId: String, isFollowing: Boolean) {
        val followingRef = firestoreDb.collection("following").document(currentUserId)
        val followerRef = firestoreDb.collection("followers").document(userId)

        if (isFollowing) {
            viewModelScope.launch {

                followingRef.update("followingIds", FieldValue.arrayRemove(userId))
                followerRef.update("followerIds", FieldValue.arrayRemove(currentUserId))
            }
        } else {
            viewModelScope.launch {

                followingRef.update("followingIds", FieldValue.arrayUnion(userId))
                followerRef.update("followerIds", FieldValue.arrayUnion(currentUserId))
            }
        }
    }

    fun getFollowers(userId: String) {

        firestoreDb.collection("followers").document(userId)
            .addSnapshotListener { snapshot, error ->

                val followerIds = snapshot?.get("followerIds") as? List<String> ?: listOf()
                _followerList.postValue(followerIds)
            }
    }

    fun getFollowing(userId: String) {
        firestoreDb.collection("following").document(userId)
            .addSnapshotListener { snapshot, error ->

                val followerIds = snapshot?.get("followingIds") as? List<String> ?: listOf()
                _followingList.postValue(followerIds)
            }
    }

    fun fetchChatMessages(chatId: String) {
        val storyKey = chatRef.push().key ?: return

        chatRef.child(chatId).orderByChild("timestamp")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    viewModelScope.launch {

                        val chatList = snapshot.children.mapNotNull {
                            it.getValue(ChatModel::class.java)
                        }
                        _chatMessages.postValue(chatList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Chat", "Failed to fetch chat messages: ${error.message}")
                }
            })
    }

    // Send a chat message
    fun sendMessage(chatId: String, message: ChatModel) {
        val messageWithStoreKey = message.copy(storeKey = chatRef.push().key ?: "")
        viewModelScope.launch {

            chatRef.child(chatId).push().setValue(messageWithStoreKey)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Chat", "Message sent successfully")
                    } else {
                        Log.d("Chat", "Failed to send message: ${task.exception?.message}")
                    }
                }
        }
    }

    // Delete a chat message
    fun deleteMessage(chatId: String, storeKey: String) {
        chatRef.child(chatId).orderByChild("storeKey").equalTo(storeKey)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    viewModelScope.launch {

                        if (snapshot.exists()) {
                            // Iterate through all matching messages with the given storeKey
                            snapshot.children.forEach { messageSnapshot ->
                                val messageId = messageSnapshot.key
                                if (messageId != null) {
                                    chatRef.child(chatId).child(messageId).removeValue()
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Log.d(
                                                    "Chat",
                                                    "Message with storeKey $storeKey deleted successfully"
                                                )
                                            } else {
                                                Log.d(
                                                    "Chat",
                                                    "Failed to delete message with storeKey $storeKey: ${task.exception?.message}"
                                                )
                                            }
                                        }
                                }
                            }
                        } else {
                            Log.d("Chat", "No message found with storeKey $storeKey")
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(
                        "Chat",
                        "Failed to delete message with storeKey $storeKey: ${error.message}"
                    )
                }
            })
    }

}

