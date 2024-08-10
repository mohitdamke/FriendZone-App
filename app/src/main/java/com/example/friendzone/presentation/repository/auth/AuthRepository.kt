package com.example.friendzone.presentation.repository.auth

import android.content.Context
import android.net.Uri
import com.example.friendzone.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun loginUser(
        email: String,
        password: String,
        context: Context
    ): Flow<Resource<AuthResult>>

    suspend fun signOutUser(): Flow<Resource<Unit>>

    suspend fun registerUser(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: Uri,
        context: Context
    ): Flow<Resource<AuthResult>>

}