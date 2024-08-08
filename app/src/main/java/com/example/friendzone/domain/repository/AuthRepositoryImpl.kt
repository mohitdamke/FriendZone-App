package com.example.friendzone.domain.repository

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.friendzone.data.model.UserModel
import com.example.friendzone.presentation.repository.AuthRepository
import com.example.friendzone.util.Resource
import com.example.friendzone.util.SharedPref
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth

) : AuthRepository {


    val auth = FirebaseAuth.getInstance()
  private  val currentUser = auth.currentUser

    private val db = FirebaseDatabase.getInstance()
    private val userRef = db.getReference("users")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    private val firebaseUser: LiveData<FirebaseUser?> = _firebaseUser

    init {
        _firebaseUser.value = currentUser
    }

    override suspend fun loginUser(
        email: String,
        password: String,
        context: Context
    ): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            _firebaseUser.postValue(auth.currentUser)
            getData(auth.currentUser!!.uid, context)
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override suspend fun signOutUser(): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signOut()
            _firebaseUser.postValue(null)
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }


    private fun getData(uid: String, context: Context) {


        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserModel::class.java)
                SharedPref.storeData(
                    name = userData!!.name,
                    userName = userData.userName,
                    email = userData.email,
                    bio = userData.bio,
                    imageUri = userData.imageUrl,
                    context = context
                )
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override suspend fun registerUser(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: Uri,
        context: Context,
        ): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            _firebaseUser.postValue(firebaseUser.value)
            saveImage(
                email = email,
                password = password,
                name = name,
                bio = bio,
                userName = userName,
                imageUri = imageUri,
                uid = auth.currentUser?.uid,
                context = context
            )
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }


    private fun saveImage(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: Uri?,
        uid: String?,
        context: Context
    ) {

        val uploadTask = imageRef.putFile(imageUri!!)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                saveData(
                    email = email,
                    password = password,
                    name = name,
                    bio = bio,
                    userName = userName,
                    toString = uri.toString(),
                    uid = uid,
                    context = context
                )
            }
        }


    }

    private fun saveData(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        toString: String,
        uid: String?,
        context: Context
    ) {
        val firestoreDb = Firebase.firestore
        val followersRef = firestoreDb.collection("followers").document(uid!!)
        val followingRef = firestoreDb.collection("following").document(uid)

        followingRef.set(mapOf("followingIds" to listOf<String>()))
        followersRef.set(mapOf("followerIds" to listOf<String>()))

        val userData = UserModel(
            email = email,
            password = password,
            name = name,
            bio = bio,
            userName = userName,
            imageUrl = toString,
            uid = uid
        )

        userRef.child(uid).setValue(userData).addOnSuccessListener {
            SharedPref.storeData(
                name = name,
                userName = userName,
                email = email,
                bio = bio,
                imageUri = toString,
                context = context
            )
        }.addOnFailureListener {}


    }


}