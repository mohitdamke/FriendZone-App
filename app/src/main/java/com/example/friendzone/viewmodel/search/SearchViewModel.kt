package com.example.friendzone.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.friendzone.data.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val users = db.getReference("users")

    private var _users = MutableLiveData<List<UserModel>>()
    val userList: LiveData<List<UserModel>> = _users

    private fun fetchUsers(onResult: (List<UserModel>) -> Unit) {
        users.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<UserModel>()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(UserModel::class.java)
                    if (user != null) {
                        userList.add(user)
                    }
                }
                onResult(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                onResult(emptyList())
            }
        })
    }

    fun fetchUsersExcludingCurrentUser(currentUserId: String) {
        fetchUsers { userList ->
            _users.value = userList.filter { user -> user.uid != currentUserId }
        }
    }

}

